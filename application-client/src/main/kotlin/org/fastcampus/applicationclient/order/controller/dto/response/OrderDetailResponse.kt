package org.fastcampus.applicationclient.order.controller.dto.response

import org.fastcampus.order.entity.Order
import org.fastcampus.order.entity.OrderDetail
import org.fastcampus.payment.entity.Payment
import java.time.LocalDateTime

data class OrderDetailResponse(
    val orderId: String,
    val storeName: String?,
    val status: Map<String, String?>,
    val orderTime: LocalDateTime,
    val isDeleted: Boolean,
    val tel: String?,
    val roadAddress: String?,
    val jibunAddress: String?,
    val detailAddress: String?,
    val excludingSpoonAndFork: Boolean,
    val requestToRider: String?,
    val orderPrice: Long,
    val deliveryPrice: Long?,
    val deliveryCompleteTime: LocalDateTime?,
    val paymentPrice: Long,
    val paymentId: Long,
    val paymentType: Map<String, String?>,
    val type: Map<String, String?>,
    val orderMenus: List<OrderMenuResponse>?,
) {
    companion object {
        fun from(orderDetail: OrderDetail): OrderDetailResponse {
            return OrderDetailResponse(
                orderDetail.orderId,
                orderDetail.storeName,
                orderDetail.status,
                orderDetail.orderTime,
                orderDetail.isDeleted,
                orderDetail.tel,
                orderDetail.roadAddress,
                orderDetail.jibunAddress,
                orderDetail.detailAddress,
                orderDetail.excludingSpoonAndFork,
                orderDetail.requestToRider,
                orderDetail.orderPrice,
                orderDetail.deliveryPrice,
                orderDetail.deliveryCompleteTime,
                orderDetail.paymentPrice,
                orderDetail.paymentId,
                orderDetail.paymentType,
                orderDetail.type,
                orderDetail.orderMenus?.map { OrderMenuResponse.from(it) },
            )
        }

        fun from(order: Order, paymentType: Payment.Type, orderMenuResponses: List<OrderMenuResponse>): OrderDetailResponse {
            return OrderDetailResponse(
                order.id,
                order.storeName,
                mapOf("code" to order.status.code, "desc" to order.status.desc),
                order.orderTime,
                order.isDeleted,
                order.tel,
                order.roadAddress,
                order.jibunAddress,
                order.detailAddress,
                order.excludingSpoonAndFork,
                order.requestToRider,
                order.orderPrice,
                order.deliveryPrice,
                order.deliveryCompleteTime,
                order.paymentPrice,
                order.paymentId,
                mapOf("code" to paymentType.code, "desc" to paymentType.desc),
                mapOf("code" to order.type.code, "desc" to order.type.desc),
                orderMenuResponses,
            )
        }
    }
}
