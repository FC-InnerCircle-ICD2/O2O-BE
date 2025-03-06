package org.fastcampus.payment.gateway.client

import org.fastcampus.payment.gateway.config.Pay200ClientConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "pay200Client", url = "\${external.pay200.url}", configuration = [Pay200ClientConfig::class])
interface Pay200Client {
    @PostMapping("/api/v1/p/merchant/confirm")
    fun approve(request: Pay200ApproveRequest): Pay200ApproveResponse
}
