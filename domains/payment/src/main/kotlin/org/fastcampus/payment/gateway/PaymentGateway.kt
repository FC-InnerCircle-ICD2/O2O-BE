package org.fastcampus.payment.gateway

interface PaymentGateway {
    fun approve(paymentKey: String, orderId: String, amount: Long): PaymentGatewayResponse
}
