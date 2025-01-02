package org.fastcampus.applicationadmin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApplicationAdminApplication

fun main(args: Array<String>) {
    runApplication<ApplicationAdminApplication>(*args)
}
