package org.fastcampus.applicationclient.order.service

import org.fastcampus.applicationclient.order.controller.dto.request.OrderCreationRequest
import org.fastcampus.common.util.throwIfNullOrEmpty
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
            val filteredMenuEntities = confirmOrderMenus(storeMenuCategoryEntities, requestOrderMenus)

            // 주문요청 옵션그룹
            val requestOrderMenuOptionGroups = requestOrderMenus.flatMap { it.orderMenuOptionGroups }

            // 옵션그룹정보, 옵션 선택 확인
            return confirmOrderMenuOptionGroups(filteredMenuEntities, requestOrderMenuOptionGroups)
        }

        private fun Store.isClosed(): Boolean {
            return this.status == Store.Status.CLOSE
        }

        private fun confirmOrderMenuOptionGroups(
            filteredMenuEntities: Collection<Menu>,
            requestOrderMenuOptionGroups: Collection<OrderCreationRequest.OrderMenuOptionGroup>,
        ): List<Menu> {
            throwIfNullOrEmpty(
                filteredMenuEntities,
                OrderException(HttpStatus.BAD_REQUEST.value(), "가게에 등록된 메뉴 정보가 없습니다."),
            )
            throwIfNullOrEmpty(
                requestOrderMenuOptionGroups,
                OrderException(HttpStatus.BAD_REQUEST.value(), "주문 요청에 메뉴 옵션 그룹이 없습니다."),
            )

            // 주문 옵션 그룹의 ID set
            val requestOrderMenuOptionGroupIdSet = requestOrderMenuOptionGroups.mapTo(mutableSetOf()) { it.id }

            // 옵션 그룹 entity 돌며 주문 옵션 그룹 ID 확인
            val targetMenuEntities = filteredMenuEntities.map { menuEntity ->
                // 대상옵션그룹들
                val filteredMenuOptionGroupEntities = menuEntity.menuOptionGroup.orEmpty()
                    .filter { requestOrderMenuOptionGroupIdSet.remove(it.id) }

                // 메뉴 옵션 그룹 정보 미존재
                if (requestOrderMenuOptionGroupIdSet.isNotEmpty()) {
                    throw OrderException(HttpStatus.BAD_REQUEST.value(), "요청된 메뉴 옵션 그룹중 등록되어 있지 않은 그룹이 존재합니다.")
                }

                // 그룹 ID별 선택한 옵션
                val requestOrderMenuOptionsByGroupId = requestOrderMenuOptionGroups.associate {
                    it.id to it.orderMenuOptionIds.toMutableSet()
                }

                // 그룹에 해당하는 옵션 min,max 선택 확인, 옵션 정보 확인
                val targetMenuOptionGroupEntities = filteredMenuOptionGroupEntities.map { menuOptionGroupEntity ->
                    val minSel = menuOptionGroupEntity.minSel
                    val maxSel = menuOptionGroupEntity.maxSel
                    if (minSel == null || maxSel == null) {
                        throw OrderException(HttpStatus.BAD_REQUEST.value(), "옵션그룹의 최대/최소 선택 수 정보가 없습니다.")
                    }

                    val optionIds = requestOrderMenuOptionsByGroupId[menuOptionGroupEntity.id]!!
                    val targetMenuOptionEntities = menuOptionGroupEntity.menuOption
                        .throwIfNullOrEmpty(OrderException(HttpStatus.BAD_REQUEST.value(), "등록된 옵션그룹 내에 옵션 정보가 없습니다."))
                        .filter { option -> optionIds.remove(option.id) }

                    val selectedOptionIdSize = optionIds.size
                    if (selectedOptionIdSize !in minSel..maxSel) {
                        throw OrderException(HttpStatus.BAD_REQUEST.value(), "옵션그룹의 선택범위를 벗어나는 옵션이 존재합니다.")
                    }

                    // 메뉴옵션그룹 복사
                    menuOptionGroupEntity.copy(menuOption = targetMenuOptionEntities)
                }

                // 메뉴 복사
                menuEntity.copy(menuOptionGroup = targetMenuOptionGroupEntities)
            }

            return targetMenuEntities
        }

        private fun confirmOrderMenus(
            storeMenuCategoryEntities: Collection<StoreMenuCategory>?,
            requestOrderMenus: Collection<OrderCreationRequest.OrderMenu>,
        ): List<Menu> {
            throwIfNullOrEmpty(
                storeMenuCategoryEntities,
                OrderException(HttpStatus.BAD_REQUEST.value(), "가게에 등록된 메뉴 카테고리 정보가 없습니다."),
            )
            throwIfNullOrEmpty(
                requestOrderMenus,
                OrderException(HttpStatus.BAD_REQUEST.value(), "주문 요청에 메뉴정보가 없습니다."),
            )

            // 주문 메뉴들의 ID set
            val requestOrderMenuIdSet = requestOrderMenus.mapTo(mutableSetOf()) { it.id }

            // 메뉴 entity 돌며 주문 메뉴 ID 확인
            val filteredMenuEntities = storeMenuCategoryEntities!!
                .flatMap { menuCategory -> menuCategory.menu.orEmpty() }
                .filter { menu -> requestOrderMenuIdSet.remove(menu.id) }

            // 주문 메뉴 정보 미존재
            if (requestOrderMenuIdSet.isNotEmpty()) {
                throw OrderException(HttpStatus.BAD_REQUEST.value(), "요청한 메뉴중 등록되어 있지 않은 메뉴가 있습니다.")
            }

            // 품절 메뉴가 있는지 확인
            filteredMenuEntities
                .filter { it.isSoldOut }
                .any { throw OrderException(HttpStatus.BAD_REQUEST.value(), "품절된 메뉴가 존재합니다.") }

            return filteredMenuEntities
        }
    }
}
