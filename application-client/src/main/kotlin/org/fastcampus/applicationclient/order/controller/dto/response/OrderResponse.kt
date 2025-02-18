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
    val deliveryCompleteTime: LocalDateTime?,
    val paymentPrice: Long,
)
