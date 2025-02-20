package org.fastcampus.applicationoss

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableBatchProcessing
@SpringBootApplication(
    scanBasePackages = [
        "org.fastcampus.applicationoss",
        "org.fastcampus.order",
        "org.fastcampus.payment",
    ],
)
class ApplicationOssApplication

fun main(args: Array<String>) {
    runApplication<ApplicationOssApplication>(*args)
}
