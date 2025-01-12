package org.fastcampus.store.mongo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StoreMongoApplication

fun main(args: Array<String>) {
    runApplication<StoreMongoApplication>(*args)
}
