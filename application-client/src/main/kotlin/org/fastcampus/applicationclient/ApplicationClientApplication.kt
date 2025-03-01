package org.fastcampus.applicationclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication(
    scanBasePackages = [
        "org.fastcampus.applicationclient",
        "org.fastcampus.order",
        "org.fastcampus.store",
        "org.fastcampus.member",
        "org.fastcampus.payment",
        "org.fastcampus.review",
        "org.fastcampus.cart",
        "org.fastcampus.favorite",
    ],
)
@EnableFeignClients(basePackages = ["org.fastcampus.payment.gateway.client"])
class ApplicationClientApplication

fun main(args: Array<String>) {
    runApplication<ApplicationClientApplication>(*args)
}
