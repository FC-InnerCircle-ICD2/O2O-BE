package org.fastcampus.payment.gateway.client

import org.fastcampus.payment.gateway.PaymentGateway
import org.fastcampus.payment.gateway.PaymentGatewayResponse
import org.fastcampus.payment.gateway.error.Pay200Exception
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component("pay200Gateway")
class Pay200Gateway(
    private val pay200Client: Pay200Client,
) : PaymentGateway {
    override fun approve(paymentKey: String, orderId: String, amount: Long): PaymentGatewayResponse {
        try {
            val request = Pay200ApproveRequest(
                paymentKey = paymentKey,
                orderId = orderId,
                amount = amount,
            )
            val response = pay200Client.approve(request)
            return response.toPaymentGatewayResponse()
        } catch (e: Pay200Exception) {
            val errorResponse = e.error
            logger.error("Pay200Gateway-approve-error: {}", errorResponse)
            return errorResponse?.toPaymentGatewayResponse()
                ?: PaymentGatewayResponse(
                    status = PaymentGatewayResponse.Status.FAILED,
                    message = "결제 승인 처리에 실패하였습니다.",
                )
        }
    }

    override fun cancel(paymentKey: String, orderId: String, amount: Long): PaymentGatewayResponse {
        try {
            val request = Pay200CancelRequest(amount)
            val response = pay200Client.cancel(paymentKey, request)
            return response.toPaymentGatewayResponse()
        } catch (e: Pay200Exception) {
            val errorResponse = e.error
            logger.error("Pay200Gateway-cancel-error: {}", errorResponse)

            return errorResponse?.toPaymentGatewayResponse()
                ?: PaymentGatewayResponse(
                    status = PaymentGatewayResponse.Status.FAILED,
                    message = "결제 취소 처리에 실패하였습니다.",
                )
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
