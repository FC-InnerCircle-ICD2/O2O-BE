package org.fastcampus.member.postgres

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class MemberPostgresApplication

fun main(args: Array<String>) {
    runApplication<MemberPostgresApplication>(*args)
}
