package org.fastcampus.member.postgres.repository

import org.fastcampus.member.postgres.entity.MemberJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<MemberJpaEntity, Long>
