package org.fastcampus.payment.gateway

interface PaymentGateway {
    fun approve(paymentKey: String, orderId: String, amount: Long): PaymentGatewayResponse

    fun cancel(paymentKey: String, orderId: String, amount: Long): PaymentGatewayResponse
}
