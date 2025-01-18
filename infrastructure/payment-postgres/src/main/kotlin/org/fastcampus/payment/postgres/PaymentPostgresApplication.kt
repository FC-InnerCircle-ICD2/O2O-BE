package org.fastcampus.payment.postgres

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PaymentPostgresApplication

fun main(args: Array<String>) {
    runApplication<PaymentPostgresApplication>(*args)
}
