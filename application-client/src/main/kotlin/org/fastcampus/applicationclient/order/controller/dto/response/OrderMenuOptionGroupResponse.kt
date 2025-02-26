package org.fastcampus.applicationclient.order.controller.dto.response

import org.fastcampus.order.entity.OrderMenuOptionGroup

data class OrderMenuOptionGroupResponse(
    val id: Long?,
    val orderMenuId: Long,
    val orderMenuOptionGroupName: String,
    val orderMenuOptions: List<OrderMenuOptionResponse>?,
) {
    companion object {
        fun from(orderMenuOptionGroup: OrderMenuOptionGroup): OrderMenuOptionGroupResponse {
            return OrderMenuOptionGroupResponse(
                orderMenuOptionGroup.id,
                orderMenuOptionGroup.orderMenuId,
                orderMenuOptionGroup.orderMenuOptionGroupName,
                orderMenuOptionGroup.orderMenuOptions?.map { OrderMenuOptionResponse.from(it) },
            )
        }

        fun from(
            orderMenuOptionGroup: OrderMenuOptionGroup,
            orderMenuOptionResponses: List<OrderMenuOptionResponse>,
        ): OrderMenuOptionGroupResponse {
            return OrderMenuOptionGroupResponse(
                orderMenuOptionGroup.id,
                orderMenuOptionGroup.orderMenuId,
                orderMenuOptionGroup.orderMenuOptionGroupName,
                orderMenuOptionResponses,
            )
        }
    }
}
