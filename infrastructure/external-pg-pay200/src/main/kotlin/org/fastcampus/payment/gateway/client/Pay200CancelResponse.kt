package org.fastcampus.payment.gateway.client

import org.fastcampus.payment.gateway.PaymentGatewayResponse
import org.fastcampus.payment.gateway.PaymentGatewayResponse.Status.CANCELED

data class Pay200CancelResponse(
    val paymentKey: String,
    val amount: Long,
    val status: PaymentGatewayResponse.Status,
    val reason: String,
) {
    fun toPaymentGatewayResponse(): PaymentGatewayResponse {
        return PaymentGatewayResponse(
            status = CANCELED,
            paymentKey = paymentKey,
            amount = amount,
            message = reason,
            orderId = "", // orderId는 미전달
        )
    }
}
