package org.fastcampus.applicationclient.payment.controller

import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.config.security.dto.JwtAuthenticated
import org.fastcampus.applicationclient.payment.controller.docs.PaymentControllerDocs
import org.fastcampus.applicationclient.payment.controller.dto.request.OrderPaymentApproveRequest
import org.fastcampus.applicationclient.payment.service.PaymentService
import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/payments")
class PaymentController(
    private val paymentService: PaymentService,
) : PaymentControllerDocs {
    @JwtAuthenticated
    @PostMapping("/approve")
    override fun approveOrderPayment(
        @RequestBody orderPaymentApproveRequest: OrderPaymentApproveRequest,
        @AuthenticationPrincipal authMember: AuthMember,
    ): APIResponseDTO<Void> {
        // TODO 분산락으로 먼저 결제처리가 진행중이라면 튕겨야 함.

        // 결제키를 먼저 저장
        paymentService.savePaymentKey(
            userId = authMember.id,
            orderId = orderPaymentApproveRequest.orderId,
            paymentKey = orderPaymentApproveRequest.paymentKey,
        )

        // 승인요청
        paymentService.approveOrderPayment(
            userId = authMember.id,
            orderId = orderPaymentApproveRequest.orderId,
            amount = orderPaymentApproveRequest.amount,
        )
        return APIResponseDTO(HttpStatus.OK.value(), "OK", null)
    }
}
