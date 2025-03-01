package org.fastcampus.payment.gateway.client

data class TossPaymentsApproveRequest(
    val paymentKey: String,
    val orderId: String,
    val amount: Long,
)
