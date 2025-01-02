package org.fastcampus.store.redis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StoreRedisApplication

fun main(args: Array<String>) {
    runApplication<StoreRedisApplication>(*args)
}
