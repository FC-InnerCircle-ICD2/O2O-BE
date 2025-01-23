package org.fastcampus.applicationclient.payment.controller.dto.request

data class OrderPaymentApproveRequest(
    val paymentKey: String,
    val orderId: String,
    val amount: Long,
)
