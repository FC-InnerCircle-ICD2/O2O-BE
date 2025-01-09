package org.fastcampus.applicationclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApplicationClientApplication

fun main(args: Array<String>) {
    runApplication<ApplicationClientApplication>(*args)
}
