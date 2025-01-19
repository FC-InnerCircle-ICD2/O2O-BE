package org.fastcampus.applicationclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "org.fastcampus.applicationclient",
        "org.fastcampus.order",
        "org.fastcampus.store",
        "org.fastcampus.payment",
    ],
)
class ApplicationClientApplication

fun main(args: Array<String>) {
    runApplication<ApplicationClientApplication>(*args)
}
