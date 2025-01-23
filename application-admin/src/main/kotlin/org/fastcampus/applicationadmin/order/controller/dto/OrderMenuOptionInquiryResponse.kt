package org.fastcampus.applicationadmin.order.controller.dto

import org.fastcampus.order.entity.OrderMenuOption

data class OrderMenuOptionInquiryResponse(
    val id: Long? = null,
    val orderMenuOptionGroupId: Long,
    val menuOptionName: String,
    val menuOptionPrice: Long,
) {
    companion object {
        fun from(orderMenuOption: OrderMenuOption): OrderMenuOptionInquiryResponse {
            return OrderMenuOptionInquiryResponse(
                orderMenuOption.id,
                orderMenuOption.orderMenuOptionGroupId,
                orderMenuOption.menuOptionName,
                orderMenuOption.menuOptionPrice,
            )
        }
    }
}
