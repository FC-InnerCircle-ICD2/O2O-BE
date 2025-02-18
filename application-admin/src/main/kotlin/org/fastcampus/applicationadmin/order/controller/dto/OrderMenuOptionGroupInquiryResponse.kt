package org.fastcampus.applicationadmin.order.controller.dto

import org.fastcampus.order.entity.OrderMenuOptionGroup

data class OrderMenuOptionGroupInquiryResponse(
    val id: Long? = null,
    val orderMenuId: Long,
    val orderMenuOptionGroupName: String,
    val orderMenuOptionInquiryResponses: List<OrderMenuOptionInquiryResponse>,
) {
    companion object {
        fun from(
            orderMenuOptionGroup: OrderMenuOptionGroup,
            orderMenuOptionInquiryResponses: List<OrderMenuOptionInquiryResponse>,
        ): OrderMenuOptionGroupInquiryResponse {
            return OrderMenuOptionGroupInquiryResponse(
                orderMenuOptionGroup.id,
                orderMenuOptionGroup.orderMenuId,
                orderMenuOptionGroup.orderMenuOptionGroupName,
                orderMenuOptionInquiryResponses,
            )
        }
    }
}
