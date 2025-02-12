package org.fastcampus.member.postgres.repository

import org.fastcampus.member.postgres.entity.MemberAddressJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberAddressJpaRepository : JpaRepository<MemberAddressJpaEntity, Long> {
    fun findByUserId(userId: Long): List<MemberAddressJpaEntity>
}
