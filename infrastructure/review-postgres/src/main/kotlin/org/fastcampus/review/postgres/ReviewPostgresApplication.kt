package org.fastcampus.review.postgres

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReviewPostgresApplication

fun main(args: Array<String>) {
    runApplication<ReviewPostgresApplication>(*args)
}
