package org.fastcampus.applicationclient.payment.controller

import org.fastcampus.applicationclient.payment.controller.dto.request.OrderPaymentApproveRequest
import org.fastcampus.applicationclient.payment.service.PaymentService
import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/payments")
class PaymentController(
    private val paymentService: PaymentService,
) {
    @PostMapping("/approve")
    fun approveOrderPayment(
        @RequestBody orderPaymentApproveRequest: OrderPaymentApproveRequest,
    ): APIResponseDTO<Void> {
        // 승인요청
        paymentService.approveOrderPayment(1, orderPaymentApproveRequest)
        // 승인완료시 점주에게 SSE 알림
        // TODO
        return APIResponseDTO(HttpStatus.OK.value(), "OK", null)
    }
}
