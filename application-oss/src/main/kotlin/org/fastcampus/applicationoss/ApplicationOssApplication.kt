package org.fastcampus.applicationoss

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "org.fastcampus.applicationoss",
    ],
)
class ApplicationOssApplication

fun main(args: Array<String>) {
    runApplication<ApplicationOssApplication>(*args)
}
