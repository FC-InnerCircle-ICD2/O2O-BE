package org.fastcampus.payment.gateway

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
            return PaymentGatewayResponse(
                status = PaymentGatewayResponse.Status.FAILED,
                message = "결제 승인 요청에 실패하였습니다.",
            )
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
