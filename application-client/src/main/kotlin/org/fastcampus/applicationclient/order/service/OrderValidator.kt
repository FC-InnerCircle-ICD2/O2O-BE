package org.fastcampus.applicationclient.order.service

import org.apache.commons.lang3.StringUtils
import org.fastcampus.applicationclient.order.controller.dto.request.OrderCreationRequest
import org.fastcampus.order.exception.OrderException
import org.fastcampus.store.entity.Menu
import org.fastcampus.store.entity.MenuOptionGroup
import org.fastcampus.store.entity.Store
import org.slf4j.LoggerFactory

class OrderValidator {
    companion object {
        private val logger = LoggerFactory.getLogger(OrderValidator::class.java)

        /**
         * 주문요청 정보를 검증하고, 메뉴정보를 반환
         *
         * @param storeEntity           가게정보 ENTITY
         * @param orderCreationRequest  주문요청정보 DTO
         * @return 주문요청에 대한 <메뉴 ID, DB 메뉴정보> MAP
         */
        fun checkOrderCreation(storeEntity: Store, orderCreationRequest: OrderCreationRequest): Map<String, Menu> {
            if (orderCreationRequest.orderMenus.isEmpty()) {
                throw OrderException.MissingOrderMenu()
            }

            // 주소입력 체크
            val roadAddress = orderCreationRequest.roadAddress
            val jubunAddress = orderCreationRequest.jibunAddress
            if (StringUtils.isBlank(roadAddress) && StringUtils.isBlank(jubunAddress)) {
                throw OrderException.AddressRequired()
            }

            // 영업중 아님 체크
            if (storeEntity.isClosed()) {
                throw OrderException.StoreClosed(storeEntity.id!!)
            }

            // <주문메뉴 ID, <주문메뉴 옵션그룹 ID, List<주문메뉴 옵션 ID>>
            val requestedOrderMenu = orderCreationRequest.orderMenus.map { it.toRequestedOrderMenu() }

            return checkOrderMenus(storeEntity, requestedOrderMenu)
        }

        /**
         * 주문메뉴 정보를 검증하고, 메뉴정보를 반환
         *
         * @param storeEntity           가게정보 ENTITY
         * @param requestedOrderMenus   주문메뉴 List
         * @return 주문요청에 대한 <메뉴 ID, DB 메뉴정보> MAP
         */
        fun checkOrderMenus(storeEntity: Store, requestedOrderMenus: List<RequestedOrderMenu>): Map<String, Menu> {
            // DB 메뉴 카테고리 정보
            val storeMenuCategoryEntities = storeEntity.storeMenuCategory
            storeMenuCategoryEntities ?: OrderException.MenuCategoryNotFound()

            // 주문요청 메뉴 ID Set
            val requestOrderMenuIdSet = requestedOrderMenus.mapTo(mutableSetOf()) { it.id }

            // 주문요청에 대한 DB 메뉴정보 필터링
            val filteredMenuEntities = storeMenuCategoryEntities!!
                .flatMap { it.menu ?: emptyList() }
                .filter { requestOrderMenuIdSet.remove(it.id) }
                .toList()

            // 주문요청 메뉴 정보 미존재 확인
            if (requestOrderMenuIdSet.isNotEmpty()) {
                throw OrderException.MenuNotFound(requestOrderMenuIdSet.joinToString(", "))
            }

            // 품절 확인
            filteredMenuEntities.forEach {
                if (it.isSoldOut) {
                    throw OrderException.SoldOutMenu(it.id!!)
                }
            }

            // 주문요청 대상 DB 메뉴정보
            val menuInfos = filteredMenuEntities.associateBy { it.id!! }

            // 옵션그룹정보, 옵션 선택 확인
            checkOrderMenuDetails(menuInfos, requestedOrderMenus)

            return menuInfos
        }

        private fun Store.isClosed(): Boolean {
            return this.status == Store.Status.CLOSE
        }

        /**
         * 주문 메뉴의 옵션 선택정보를 확인
         *
         * @param filteredMenuInfos     <메뉴 ID, DB 메뉴정보>
         * @param requestedOrderMenus   주문메뉴 List
         */
        private fun checkOrderMenuDetails(filteredMenuInfos: Map<String, Menu>, requestedOrderMenus: List<RequestedOrderMenu>) {
            // 주문요청 메뉴 1개씩 확인 그룹ID로 분류된 옵션ID 맵
            requestedOrderMenus.forEach { (requestedOrderMenuId, requestedOrderMenuOptionGroups) ->
                // DB 메뉴정보
                val menuInfo = filteredMenuInfos[requestedOrderMenuId]!!

                // 필수 옵션그룹 체크
                menuInfo.checkRequiredOptionGroupContaining(
                    requestedOrderMenuOptionGroups.map { it.id }.toSet(),
                )

                // 주문요청 옵션그룹 1개씩 확인
                requestedOrderMenuOptionGroups.forEach { (requestOrderMenuOptionGroupId, requestOrderMenuOptionIds) ->
                    logger.debug("requestOrderMenuOptionGroupId: {}", requestOrderMenuOptionGroupId)

                    // DB 옵션그룹 정보
                    val optionGroupInfo = menuInfo.menuOptionGroup?.find { it.id == requestOrderMenuOptionGroupId }
                        ?: throw OrderException.OptionGroupNotFound(requestOrderMenuOptionGroupId)

                    // 옵션선택 체크
                    optionGroupInfo.checkOptions(requestOrderMenuOptionIds.toSet())
                }
            }
        }

        private fun Menu.checkRequiredOptionGroupContaining(requestedOrderMenuOptionGroupIds: Set<String>) {
            // 필수 옵션그룹 ID Set
            val requiredOptionGroupIdSet = this.menuOptionGroup
                ?.filter { it.minSel != 0 }
                ?.map { it.id!! }
                ?.toMutableSet() ?: mutableSetOf()

            // 주문요청의 옵션그룹 ID Set
            val requestOptionGroupIds = requestedOrderMenuOptionGroupIds.toMutableSet()

            // 주문요청 옵션그룹에 필수 옵션그룹이 모두 포함되는지 확인
            if (!requestOptionGroupIds.containsAll(requiredOptionGroupIdSet)) {
                throw OrderException.MissingRequiredOptionGroup((requiredOptionGroupIdSet - requestOptionGroupIds).joinToString(", "))
            }
        }

        private fun MenuOptionGroup.checkOptions(requestOrderMenuOptionIds: Set<String>) {
            // DB 옵션정보
            val optionInfos = this.menuOption ?: emptyList()

            // 주문요청 옵션 ID Set
            val requestOrderMenuOptionIdSet = requestOrderMenuOptionIds.toMutableSet()

            // 주문요청에 대한 옵션 정보들
            val targetOptionInfos = optionInfos.filter { requestOrderMenuOptionIdSet.contains(it.id) }

            // 옵션그룹에 등록되지 않은 옵션 ID 확인
            requestOrderMenuOptionIdSet.removeAll(targetOptionInfos.map { it.id }.toSet())
            if (requestOrderMenuOptionIdSet.isNotEmpty()) {
                throw OrderException.OptionNotFound(requestOrderMenuOptionIdSet.joinToString(", "))
            }

            // min..max 사이로 선택했는지 확인
            if (this.minSel == null || this.maxSel == null) {
                throw OrderException.WeiredOptionGroupInfo(this.id!!)
            }
            if (targetOptionInfos.size !in this.minSel!!..this.maxSel!!) {
                throw OrderException.OutOfOptionSelectionRange(this.id!!)
            }
        }

        data class RequestedOrderMenu(
            val id: String,
            val requestedOrderMenuOptionGroups: List<RequestedOrderMenuOptionGroup>,
        ) {
            data class RequestedOrderMenuOptionGroup(
                val id: String,
                val requestedOrderMenuOptions: List<String>,
            )
        }

        private fun OrderCreationRequest.OrderMenu.toRequestedOrderMenu(): RequestedOrderMenu {
            return RequestedOrderMenu(
                id = this.id,
                requestedOrderMenuOptionGroups = this.orderMenuOptionGroups.map { it.toRequestedOrderMenuOptionGroup() },
            )
        }

        private fun OrderCreationRequest.OrderMenu.OrderMenuOptionGroup.toRequestedOrderMenuOptionGroup():
            RequestedOrderMenu.RequestedOrderMenuOptionGroup {
            return RequestedOrderMenu.RequestedOrderMenuOptionGroup(
                id = this.id,
                requestedOrderMenuOptions = this.orderMenuOptionIds.toList(),
            )
        }
    }
}
