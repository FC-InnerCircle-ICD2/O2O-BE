package org.fastcampus.applicationadmin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "org.fastcampus.applicationadmin",
        "org.fastcampus.member",
    ],
)
class ApplicationAdminApplication

fun main(args: Array<String>) {
    runApplication<ApplicationAdminApplication>(*args)
}
