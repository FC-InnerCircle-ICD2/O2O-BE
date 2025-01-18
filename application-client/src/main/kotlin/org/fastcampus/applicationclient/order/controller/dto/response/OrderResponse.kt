package org.fastcampus.applicationclient.order.controller.dto.response

import java.time.LocalDateTime

data class OrderResponse(
    val storeId: String?,
    val storeName: String?,
    val imageThumbnail: String?,
    val orderId: String,
    val status: Map<String, String>,
    val orderTime: LocalDateTime,
    val orderSummary: String?,
    val isDeleted: Boolean,
    val deliveryCompleteTime: LocalDateTime?,
    val tel: String?,
    val roadAddress: String?,
    val jibunAddress: String?,
    val detailAddress: String?,
    val paymentPrice: Long,
    val orderPrice: Long,
    val deliveryPrice: Long?,
    val type: Map<String, String>,
)
