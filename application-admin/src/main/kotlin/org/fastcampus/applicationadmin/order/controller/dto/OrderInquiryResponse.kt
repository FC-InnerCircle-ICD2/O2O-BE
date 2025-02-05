package org.fastcampus.applicationadmin.order.controller.dto

import org.fastcampus.order.entity.Order
import org.fastcampus.order.entity.Order.Type
import java.time.LocalDateTime

data class OrderInquiryResponse(
    val orderId: String,
    val orderName: String,
    val orderStatus: Order.ClientStatus,
    val orderType: Type,
    val orderTime: LocalDateTime,
    val totalPrice: Long,
    val totalMenuCount: Long,
    val roadAddress: String?,
    val jibunAddress: String?,
    val detailAddress: String?,
    val excludingSpoonAndFork: Boolean,
    val requestToRider: String?,
    val orderMenuInquiryResponses: List<OrderMenuInquiryResponse>,
) {
    companion object {
        fun from(order: Order, orderMenuInquiryResponses: List<OrderMenuInquiryResponse>): OrderInquiryResponse {
            return OrderInquiryResponse(
                orderId = order.id,
                orderName = order.orderSummary ?: "",
                orderStatus = order.status.toClientStatus(),
                orderType = order.type,
                orderTime = order.orderTime,
                totalPrice = order.paymentPrice,
                totalMenuCount = orderMenuInquiryResponses
                    .map { it.menuQuantity }
                    .fold(0) { acc, quantity -> acc + quantity },
                roadAddress = order.roadAddress ?: "",
                jibunAddress = order.jibunAddress ?: "",
                detailAddress = order.detailAddress ?: "",
                excludingSpoonAndFork = order.excludingSpoonAndFork,
                requestToRider = order.requestToRider,
                orderMenuInquiryResponses = orderMenuInquiryResponses,
            )
        }
    }
}
