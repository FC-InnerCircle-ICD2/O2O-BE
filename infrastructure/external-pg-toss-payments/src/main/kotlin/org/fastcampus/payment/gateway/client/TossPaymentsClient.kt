package org.fastcampus.payment.gateway.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "tossPaymentsClient", url = "\${external.tosspayments.url}")
interface TossPaymentsClient {
    @PostMapping("/v1/payments/confirm")
    fun approve(request: TossPaymentsApproveRequest): TossPaymentsResponse
}
