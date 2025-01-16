package org.fastcampus.applicationclient


import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = ["org.fastcampus.applicationclient", "org.fastcampus.store"])
class ApplicationClientApplication

fun main(args: Array<String>) {
    runApplication<ApplicationClientApplication>(*args)
}
