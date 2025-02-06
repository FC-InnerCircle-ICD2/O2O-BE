package org.fastcampus.applicationclient.order.controller.dto.response

import java.time.LocalDateTime

data class OrderDetailResponse(
    val orderId: String,
    val storeName: String,
    val status: Map<String, String>,
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
    val paymentType: Map<String, String>,
    val type: Map<String, String>,
    val orderMenus: List<OrderMenuResponse>,
)
