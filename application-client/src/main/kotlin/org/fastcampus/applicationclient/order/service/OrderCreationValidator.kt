package org.fastcampus.applicationclient.order.service

import org.fastcampus.applicationclient.order.controller.dto.request.OrderCreationRequest
import org.fastcampus.order.exception.OrderException
import org.fastcampus.store.entity.Menu
import org.fastcampus.store.entity.Store
import org.fastcampus.store.entity.StoreMenuCategory
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus

class OrderCreationValidator {
    companion object {
        private val logger = LoggerFactory.getLogger(OrderCreationValidator::class.java)

        /**
         * 주문요청 정보를 검증하고, 메뉴정보를 반환
         *
         * @param storeEntity           가게정보 ENTITY
         * @param orderCreationRequest  주문요청정보 DTO
         * @return 주문 메뉴에 대한 ENTITY 리스트
         */
        fun validate(
            storeEntity: Store,
            orderCreationRequest: OrderCreationRequest,
        ): Map<String, Menu> {
            if (storeEntity.isClosed()) {
                throw OrderException(HttpStatus.BAD_REQUEST.value(), "가게의 영업이 종료되었습니다.")
            }

            val storeMenuCategoryEntities = storeEntity.storeMenuCategory
            val requestOrderMenus = orderCreationRequest.orderMenus

            // 주문요청 메뉴정보
            val filteredMenuInfos = mapMenuInfoByRequestMenuId(storeMenuCategoryEntities, requestOrderMenus)

            // 옵션그룹정보, 옵션 선택 확인
            confirmOrderMenuDetails(filteredMenuInfos, requestOrderMenus)

            return filteredMenuInfos
        }

        private fun Store.isClosed(): Boolean {
            return this.status == Store.Status.CLOSE
        }

        private fun confirmOrderMenuDetails(
            filteredMenuInfos: Map<String, Menu>,
            requestOrderMenus: Collection<OrderCreationRequest.OrderMenu>,
        ) {
            // 주문요청 메뉴 1개씩 확인
            requestOrderMenus.forEach { requestOrderMenu ->
                // DB 메뉴정보
                val menuInfo = filteredMenuInfos[requestOrderMenu.id]!!

                // 필수 옵션그룹
                val requiredOptionGroupIdSet = menuInfo.menuOptionGroup
                    ?.filter { it.minSel != 0 }
                    ?.map { it.id }
                    ?.toMutableSet() ?: emptySet()
                // 주문요청 옵션그룹 ID
                val requestOptionGroupIds = requestOrderMenu.orderMenuOptionGroups.mapTo(mutableSetOf()) { it.id }

                // 주문요청 옵션그룹에 필수 옵션그룹이 모두 포함되는지 확인
                if (!requestOptionGroupIds.containsAll(requiredOptionGroupIdSet)) {
                    logger.debug("누락된 필수 옵션그룹: {}", requiredOptionGroupIdSet - requestOptionGroupIds)
                    throw OrderException(HttpStatus.BAD_REQUEST.value(), "필수 옵션그룹이 누락되었습니다.")
                }

                // 주문요청 옵션그룹 1개씩 확인
                requestOrderMenu.orderMenuOptionGroups.forEach { requestOrderMenuOptionGroup ->
                    logger.debug("requestOrderMenuOptionGroup: {}", requestOrderMenuOptionGroup)
                    // DB 옵션그룹 정보
                    val optionGroupInfo = menuInfo.menuOptionGroup?.find { it.id == requestOrderMenuOptionGroup.id }
                        ?: throw OrderException(HttpStatus.BAD_REQUEST.value(), "요청된 옵션그룹중 가게에 등록되어 있지 않은 그룹이 존재합니다.")

                    // DB 옵션정보
                    val optionInfos = optionGroupInfo.menuOption ?: emptyList()

                    // 주문요청 옵션 ID 목록
                    val requestOrderMenuOptionIds = requestOrderMenuOptionGroup.orderMenuOptionIds.toMutableSet()

                    // 주문요청 옵션 정보만 필터링
                    val targetOptionInfos = optionInfos.filter { requestOrderMenuOptionIds.remove(it.id) }

                    // 옵션그룹에 없는 옵션 ID 확인
                    if (requestOrderMenuOptionIds.isNotEmpty()) {
                        logger.debug("등록되어 있지 않은 옵션: {}", requestOrderMenuOptionIds)
                        throw OrderException(HttpStatus.BAD_REQUEST.value(), "요청된 옵션중 가게에 등록되어 있지 않은 옵션이 존재합니다.")
                    }
                    // min, max 값 확인
                    val minSel = optionGroupInfo.minSel
                    val maxSel = optionGroupInfo.maxSel
                    if (minSel == null || maxSel == null) {
                        logger.debug("올바르지 않은 옵션그룹 선택정보: {}", optionGroupInfo.id)
                        throw OrderException(HttpStatus.BAD_REQUEST.value(), "등록된 옵션그룹 선택정보가 올바르지 않습니다.")
                    }
                    // min..max 사이로 선택했는지 확인
                    if (targetOptionInfos.size !in minSel..maxSel) {
                        logger.debug("올바르지 않은 min..max 선택: {}", optionGroupInfo.id)
                        throw OrderException(HttpStatus.BAD_REQUEST.value(), "옵션그룹의 선택범위를 벗어나는 옵션이 존재합니다.")
                    }
                }
            }
        }

        private fun mapMenuInfoByRequestMenuId(
            storeMenuCategoryEntities: Collection<StoreMenuCategory>?,
            requestOrderMenus: Collection<OrderCreationRequest.OrderMenu>,
        ): Map<String, Menu> {
            storeMenuCategoryEntities ?: OrderException(HttpStatus.BAD_REQUEST.value(), "가게에 등록된 메뉴 카테고리 정보가 없습니다.")

            if (requestOrderMenus.isEmpty()) {
                throw OrderException(HttpStatus.BAD_REQUEST.value(), "주문 요청에 메뉴정보가 없습니다.")
            }
            // 주문 메뉴들의 ID set
            val requestOrderMenuIdSet = requestOrderMenus.mapTo(mutableSetOf()) { it.id }

            // 주문 메뉴 ID 확인하며 대상 메뉴정보 필터링
            val filteredMenuEntities = storeMenuCategoryEntities!!
                .asSequence()
                .flatMap { menuCategory -> menuCategory.menu ?: emptyList() }
                .filter { menu ->
                    val removed = requestOrderMenuIdSet.remove(menu.id)
                    // 품절 확인
                    if (removed && menu.isSoldOut) {
                        logger.debug("품절메뉴: {}", menu.id)
                        throw OrderException(HttpStatus.BAD_REQUEST.value(), "품절된 메뉴가 존재합니다.")
                    }
                    removed
                }
                .toList()

            // 주문 메뉴 정보 미존재
            if (requestOrderMenuIdSet.isNotEmpty()) {
                logger.debug("등록되어 있지 않은 메뉴: {}", requestOrderMenuIdSet)
                throw OrderException(HttpStatus.BAD_REQUEST.value(), "가게에 등록되어 있지 않은 메뉴가 있습니다.")
            }

            return filteredMenuEntities.associateBy { it.id!! }
        }
    }
}
