package org.fastcampus.applicationclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
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
