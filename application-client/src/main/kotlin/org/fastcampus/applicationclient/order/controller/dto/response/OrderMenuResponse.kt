package org.fastcampus.applicationclient.order.controller.dto.response

import org.fastcampus.order.entity.OrderMenu

data class OrderMenuResponse(
    val id: Long?,
    val menuId: String,
    val menuName: String,
    val menuQuantity: Long,
    val menuPrice: Long,
    val totalPrice: Long,
    val orderMenuOptionGroups: List<OrderMenuOptionGroupResponse>?,
) {
    companion object {
        fun from(orderMenu: OrderMenu): OrderMenuResponse {
            return OrderMenuResponse(
                orderMenu.id,
                orderMenu.menuId,
                orderMenu.menuName,
                orderMenu.menuQuantity,
                orderMenu.menuPrice,
                orderMenu.totalPrice,
                orderMenu.orderMenuOptionGroups?.map { OrderMenuOptionGroupResponse.from(it) },
            )
        }

        fun from(orderMenu: OrderMenu, orderMenuOptionGroupResponses: List<OrderMenuOptionGroupResponse>): OrderMenuResponse {
            return OrderMenuResponse(
                orderMenu.id,
                orderMenu.menuId,
                orderMenu.menuName,
                orderMenu.menuQuantity,
                orderMenu.menuPrice,
                orderMenu.totalPrice,
                orderMenuOptionGroupResponses,
            )
        }
    }
}
