package org.fastcampus.applicationclient

import org.fastcampus.member.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApplicationClientApplication

fun main(args: Array<String>) {
    runApplication<ApplicationClientApplication>(*args)
    val test = Test("sdfsdf")
}
