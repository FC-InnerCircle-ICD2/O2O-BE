package org.fastcampus.payment.gateway

data class TossPaymentsApproveRequest(
    val paymentKey: String,
    val orderId: String,
    val amount: Long,
)
