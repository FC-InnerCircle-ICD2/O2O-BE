package org.fastcampus.order.postgres

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OrderPostgresApplication

fun main(args: Array<String>) {
    runApplication<OrderPostgresApplication>(*args)
}
