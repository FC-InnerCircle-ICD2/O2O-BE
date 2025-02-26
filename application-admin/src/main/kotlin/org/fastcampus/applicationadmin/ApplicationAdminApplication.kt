package org.fastcampus.applicationadmin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

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
@EnableScheduling
class ApplicationAdminApplication

fun main(args: Array<String>) {
    runApplication<ApplicationAdminApplication>(*args)
}
