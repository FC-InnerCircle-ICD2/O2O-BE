package org.fastcampus.applicationclient.order.controller.dto.response

import org.fastcampus.order.entity.OrderMenuOption

data class OrderMenuOptionResponse(
    val id: Long?,
    val orderMenuOptionGroupId: Long,
    val menuOptionName: String,
    val menuOptionPrice: Long,
) {
    companion object {
        fun from(orderMenuOption: OrderMenuOption): OrderMenuOptionResponse {
            return OrderMenuOptionResponse(
                orderMenuOption.id,
                orderMenuOption.orderMenuOptionGroupId,
                orderMenuOption.menuOptionName,
                orderMenuOption.menuOptionPrice,
            )
        }
    }
}
