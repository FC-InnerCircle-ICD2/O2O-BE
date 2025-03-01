package org.fastcampus.payment.gateway.client

import org.fastcampus.payment.gateway.PaymentGatewayResponse

/**
 * https://docs.tosspayments.com/reference#payment-객체
 */
data class TossPaymentsResponse(
    val status: Status,
    val mId: String = "",
    val paymentKey: String = "",
    val orderId: String = "",
    val orderName: String = "",
    val totalAmount: Long = 0,
) {
    enum class Status {
        READY,
        IN_PROGRESS,
        WAITING_FOR_DEPOSIT,
        DONE,
        CANCELED,
        PARTIAL_CANCELED,
        ABORTED,
        EXPIRED,
    }

    fun toPaymentGatewayResponse(): PaymentGatewayResponse {
        val status = when (this.status) {
            Status.EXPIRED -> PaymentGatewayResponse.Status.EXPIRED
            Status.DONE -> PaymentGatewayResponse.Status.DONE
            Status.ABORTED -> PaymentGatewayResponse.Status.ABORTED
            Status.CANCELED -> PaymentGatewayResponse.Status.CANCELED
            Status.PARTIAL_CANCELED -> TODO()
            Status.READY -> TODO()
            Status.IN_PROGRESS -> TODO()
            Status.WAITING_FOR_DEPOSIT -> TODO()
        }
        return PaymentGatewayResponse(
            status = status,
            paymentKey = paymentKey,
            orderId = orderId,
            amount = totalAmount,
        )
    }
}
