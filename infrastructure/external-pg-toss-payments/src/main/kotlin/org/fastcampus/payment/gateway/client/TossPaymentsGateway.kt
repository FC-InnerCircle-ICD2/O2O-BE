package org.fastcampus.payment.gateway.client

import org.fastcampus.payment.gateway.PaymentGateway
import org.fastcampus.payment.gateway.PaymentGatewayResponse
import org.fastcampus.payment.gateway.error.TossPaymentsException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component("tossPaymentsGateway")
class TossPaymentsGateway(
    private val tossPaymentsClient: TossPaymentsClient,
) : PaymentGateway {
    override fun approve(paymentKey: String, orderId: String, amount: Long): PaymentGatewayResponse {
        try {
            val request = TossPaymentsApproveRequest(
                paymentKey = paymentKey,
                orderId = orderId,
                amount = amount,
            )
            val response = tossPaymentsClient.approve(request)
            return response.toPaymentGatewayResponse()
        } catch (e: TossPaymentsException) {
            val errorResponse = e.error
            logger.error("TossPaymentsGateway-approve-error: {}", errorResponse)
            return errorResponse!!.toPaymentGatewayResponse()
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
