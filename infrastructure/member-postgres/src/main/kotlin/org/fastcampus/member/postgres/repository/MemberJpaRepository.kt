package org.fastcampus.member.postgres.repository

import org.fastcampus.member.postgres.entity.MemberJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface MemberJpaRepository : JpaRepository<MemberJpaEntity, Long> {
    fun findBySignname(username: String): Optional<MemberJpaEntity>
}
