package org.fastcampus.applicationclient.order.controller.dto.response

data class OrderCreationResponse(
    val orderId: String,
    val orderSummary: String,
    val totalPrice: Long,
)
