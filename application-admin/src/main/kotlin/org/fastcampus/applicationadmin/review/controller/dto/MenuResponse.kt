package org.fastcampus.applicationadmin.review.controller.dto

import org.fastcampus.order.entity.OrderMenu

data class MenuResponse(
    val menuName: String,
    val menuQuantity: Long,
    val menuOptionGroups: List<MenuOptionGroupResponse>,
) {
    companion object {
        fun of(orderMenu: OrderMenu): MenuResponse {
            return MenuResponse(
                menuName = orderMenu.menuName,
                menuQuantity = orderMenu.menuQuantity,
                menuOptionGroups = orderMenu.orderMenuOptionGroups?.map { it -> MenuOptionGroupResponse.of(it) } ?: emptyList(),
            )
        }
    }
}
