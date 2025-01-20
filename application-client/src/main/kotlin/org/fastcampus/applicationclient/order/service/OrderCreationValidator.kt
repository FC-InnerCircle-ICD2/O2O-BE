package org.fastcampus.applicationclient.order.service

import org.fastcampus.applicationclient.order.controller.dto.request.OrderCreationRequest
import org.fastcampus.order.exception.OrderException
import org.fastcampus.store.entity.Menu
import org.fastcampus.store.entity.Store
import org.fastcampus.store.entity.StoreMenuCategory
import org.springframework.http.HttpStatus

class OrderCreationValidator {
    companion object {
        fun validate(storeEntity: Store, orderCreationRequest: OrderCreationRequest): List<Menu> {
            if (storeEntity.isClosed()) {
                throw OrderException(HttpStatus.BAD_REQUEST.value(), "가게의 영업이 종료되었습니다.")
            }

            val storeMenuCategoryEntities = storeEntity.storeMenuCategory
            val requestOrderMenus = orderCreationRequest.orderMenus

            // 메뉴정보 확인
            val filteredMenuInfos = confirmOrderMenus(storeMenuCategoryEntities, requestOrderMenus)

            // 옵션그룹정보, 옵션 선택 확인
            return confirmOrderMenuOptionGroups(filteredMenuInfos, requestOrderMenus)
        }

        private fun Store.isClosed(): Boolean {
            return this.status == Store.Status.CLOSE
        }

        private fun confirmOrderMenuOptionGroups(
            filteredMenuInfos: Map<String, Menu>,
            requestOrderMenus: Collection<OrderCreationRequest.OrderMenu>,
        ): List<Menu> {
            // 주문요청 메뉴 1개씩 확인
            return requestOrderMenus.map { requestOrderMenu ->
                // DB 메뉴정보
                val menuInfo = filteredMenuInfos[requestOrderMenu.id]!!

                // 필수 옵션그룹
                val requiredOptionGroupIdSet = menuInfo.menuOptionGroup
                    ?.filter { it.minSel != 0 }
                    ?.map { it.id }
                    ?.toSet() ?: emptySet()
                // 주문요청 옵션그룹 ID
                val requestOptionGroupIds = requestOrderMenu.orderMenuOptionGroups.map { it.id }

                // 주문요청 옵션그룹에 필수 옵션그룹이 모두 포함되는지 확인
                if (!requestOptionGroupIds.containsAll(requiredOptionGroupIdSet)) {
                    throw OrderException(HttpStatus.BAD_REQUEST.value(), "필수 옵션그룹이 누락되었습니다.")
                }

                // 주문요청 옵션그룹 1개씩 확인
                val targetMenuOptionGroupEntities = requestOrderMenu.orderMenuOptionGroups.map { requestOrderMenuOptionGroup ->
                    // DB 옵션그룹 정보
                    val optionGroupInfo = menuInfo.menuOptionGroup?.find { it.id == requestOrderMenuOptionGroup.id }
                        ?: throw OrderException(HttpStatus.NOT_FOUND.value(), "요청된 옵션그룹중 가게에 등록되어 있지 않은 그룹이 존재합니다.")

                    // DB 옵션정보
                    val optionInfos = optionGroupInfo.menuOption ?: emptyList()
                    // 주문요청 옵션 ID 목록
                    val requestOrderMenuOptionIds = requestOrderMenuOptionGroup.orderMenuOptionIds.toMutableSet()
                    // 주문요청 옵션 정보만 필터링
                    val targetOptionInfos = optionInfos.filter { requestOrderMenuOptionIds.remove(it.id) }

                    // 옵션그룹에 없는 옵션 ID 확인
                    if (requestOrderMenuOptionIds.isNotEmpty()) {
                        throw OrderException(HttpStatus.NOT_FOUND.value(), "요청된 옵션중 가게에 등록되어 있지 않은 옵션이 존재합니다.")
                    }

                    // min, max 값 확인
                    val minSel = optionGroupInfo.minSel
                    val maxSel = optionGroupInfo.maxSel
                    if (minSel == null || maxSel == null) {
                        throw OrderException(HttpStatus.BAD_REQUEST.value(), "등록된 옵션그룹 선택정보가 올바르지 않습니다.")
                    }
                    // min..max 사이로 선택했는지 확인
                    if (targetOptionInfos.size !in minSel..maxSel) {
                        throw OrderException(HttpStatus.BAD_REQUEST.value(), "옵션그룹의 선택범위를 벗어나는 옵션이 존재합니다.")
                    }

                    // 메뉴옵션그룹 복사
                    optionGroupInfo.copy(menuOption = targetOptionInfos)
                }
                // 메뉴 복사
                menuInfo.copy(menuOptionGroup = targetMenuOptionGroupEntities)
            }
        }

        private fun confirmOrderMenus(
            storeMenuCategoryEntities: Collection<StoreMenuCategory>?,
            requestOrderMenus: Collection<OrderCreationRequest.OrderMenu>,
        ): Map<String, Menu> {
            storeMenuCategoryEntities ?: OrderException(HttpStatus.NOT_FOUND.value(), "가게에 등록된 메뉴 카테고리 정보가 없습니다.")
            if (requestOrderMenus.isEmpty()) {
                throw OrderException(HttpStatus.BAD_REQUEST.value(), "주문 요청에 메뉴정보가 없습니다.")
            }
            // 주문 메뉴들의 ID set
            val requestOrderMenuIdSet = requestOrderMenus.mapTo(mutableSetOf()) { it.id }

            // 메뉴 entity 돌며 주문 메뉴 ID 확인
            val filteredMenuEntities = storeMenuCategoryEntities!!
                .flatMap { menuCategory -> menuCategory.menu.orEmpty() }
                .filter { menu -> requestOrderMenuIdSet.remove(menu.id) }

            // 주문 메뉴 정보 미존재
            if (requestOrderMenuIdSet.isNotEmpty()) {
                throw OrderException(HttpStatus.NOT_FOUND.value(), "가게에 등록되어 있지 않은 메뉴가 있습니다.")
            }

            // 품절 메뉴가 있는지 확인
            filteredMenuEntities
                .filter { it.isSoldOut }
                .any { throw OrderException(HttpStatus.BAD_REQUEST.value(), "품절된 메뉴가 존재합니다.") }

            return filteredMenuEntities.associateBy { it.id!! }
        }
    }
}
