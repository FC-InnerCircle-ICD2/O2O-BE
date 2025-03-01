package org.fastcampus.payment.gateway.client

import org.fastcampus.payment.gateway.PaymentGatewayResponse

/**
 * https://documenter.getpostman.com/view/6302762/2sAYXCkJsP#eeaccd42-4cb5-499a-8a64-e6bd78d8da60
 */
data class Pay200ApproveResponse(
    val ok: Boolean = false,
    val data: Data = Data(),
) {
    data class Data(
        val orderId: String = "",
        val paymentKey: String = "",
        val amount: Long = 0L,
    )

    fun toPaymentGatewayResponse(): PaymentGatewayResponse {
        return PaymentGatewayResponse(
            status = PaymentGatewayResponse.Status.DONE,
            paymentKey = data.paymentKey,
            orderId = data.orderId,
            amount = data.amount,
        )
    }
}
