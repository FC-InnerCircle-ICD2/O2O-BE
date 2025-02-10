package org.fastcampus.applicationclient.payment.controller

import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.config.security.dto.JwtAuthenticated
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
) {
    @JwtAuthenticated
    @PostMapping("/approve")
    fun approveOrderPayment(
        @RequestBody orderPaymentApproveRequest: OrderPaymentApproveRequest,
        @AuthenticationPrincipal authMember: AuthMember,
    ): APIResponseDTO<Void> {
        // 승인요청
        paymentService.approveOrderPayment(authMember.id, orderPaymentApproveRequest)
        return APIResponseDTO(HttpStatus.OK.value(), "OK", null)
    }
}
