package org.fastcampus.applicationadmin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "org.fastcampus.applicationadmin",
        "org.fastcampus.order",
        "org.fastcampus.store",
        "org.fastcampus.member",
        "org.fastcampus.payment",
        "org.fastcampus.review",
    ],
)
class ApplicationAdminApplication

fun main(args: Array<String>) {
    runApplication<ApplicationAdminApplication>(*args)
}
