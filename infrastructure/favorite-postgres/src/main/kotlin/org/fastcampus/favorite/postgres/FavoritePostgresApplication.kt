package org.fastcampus.favorite.postgres

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FavoritePostgresApplication

fun main(args: Array<String>) {
    runApplication<FavoritePostgresApplication>(*args)
}
