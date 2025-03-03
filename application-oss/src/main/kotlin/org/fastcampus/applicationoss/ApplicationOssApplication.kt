package org.fastcampus.applicationoss

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableBatchProcessing
@SpringBootApplication(
    scanBasePackages = [
        "org.fastcampus.applicationoss",
        "org.fastcampus.order",
        "org.fastcampus.payment",
        "org.fastcampus.member",
    ],
)
@EnableFeignClients(basePackages = ["org.fastcampus.payment.gateway.client"])
class ApplicationOssApplication

fun main(args: Array<String>) {
    runApplication<ApplicationOssApplication>(*args)
}
