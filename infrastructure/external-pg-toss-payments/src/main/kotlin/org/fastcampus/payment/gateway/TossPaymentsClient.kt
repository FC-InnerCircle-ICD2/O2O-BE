package org.fastcampus.payment.gateway

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "tossPaymentsClient", url = "\${external.tosspayments.url}", configuration = [TossPaymentsClientConfig::class])
interface TossPaymentsClient {
    @PostMapping("/v1/payments/confirm")
    fun approve(request: TossPaymentsApproveRequest): TossPaymentsResponse
}
