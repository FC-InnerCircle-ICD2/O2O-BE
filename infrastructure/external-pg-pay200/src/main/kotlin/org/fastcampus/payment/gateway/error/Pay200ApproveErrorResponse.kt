package org.fastcampus.payment.gateway.error

import org.fastcampus.payment.gateway.PaymentGatewayResponse

internal data class Pay200ApproveErrorResponse(
    val ok: Boolean = false,
    val error: Error = Error(),
) {
    data class Error(
        val code: String = "",
        val message: String = "",
    )

    fun toPaymentGatewayResponse(): PaymentGatewayResponse {
        return PaymentGatewayResponse(
            status = PaymentGatewayResponse.Status.FAILED,
            message = "결제 승인 처리에 실패하였습니다.",
        )
    }
}
