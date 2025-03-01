package org.fastcampus.payment.gateway.client

data class Pay200ApproveRequest(
    val paymentKey: String,
    val orderId: String,
    val amount: Long,
)
