package org.fastcampus.applicationadmin.review.controller.dto

import org.fastcampus.order.entity.OrderMenuOptionGroup

data class MenuOptionGroupResponse(
    val orderMenuOptionGroupName: String,
    val orderMenuOptionInquiryResponses: List<MenuOptionResponse>,
) {
    companion object {
        fun of(orderMenuOptionGroup: OrderMenuOptionGroup): MenuOptionGroupResponse {
            return MenuOptionGroupResponse(
                orderMenuOptionGroupName = orderMenuOptionGroup.orderMenuOptionGroupName,
                orderMenuOptionInquiryResponses = MenuOptionResponse.of(orderMenuOptionGroup.orderMenuOptions ?: emptyList()),
            )
        }
    }
}
