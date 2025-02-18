package org.fastcampus.applicationadmin.order.controller.dto

import org.fastcampus.order.entity.OrderMenu

data class OrderMenuInquiryResponse(
    val id: Long? = null,
    val orderId: String?,
    val menuId: String,
    val menuName: String,
    val menuQuantity: Long,
    val menuPrice: Long,
    val totalPrice: Long,
    val orderMenuOptionGroupInquiryResponses: List<OrderMenuOptionGroupInquiryResponse>,
) {
    companion object {
        fun from(
            orderMenu: OrderMenu,
            orderMenuOptionGroupInquiryResponses: List<OrderMenuOptionGroupInquiryResponse>,
        ): OrderMenuInquiryResponse {
            return OrderMenuInquiryResponse(
                orderMenu.id,
                orderMenu.orderId,
                orderMenu.menuId,
                orderMenu.menuName,
                orderMenu.menuQuantity,
                orderMenu.menuPrice,
                orderMenu.totalPrice,
                orderMenuOptionGroupInquiryResponses,
            )
        }
    }
}
