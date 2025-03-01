package org.fastcampus.payment.gateway.client

import org.fastcampus.payment.gateway.config.TossPaymentsClientConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "tossPaymentsClient", url = "\${external.tosspayments.url}", configuration = [TossPaymentsClientConfig::class])
interface TossPaymentsClient {
    @PostMapping("/v1/payments/confirm")
    fun approve(request: TossPaymentsApproveRequest): TossPaymentsResponse
}
