package org.fastcampus.order.mongo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OrderMongoApplication

fun main(args: Array<String>) {
    runApplication<OrderMongoApplication>(*args)
}
