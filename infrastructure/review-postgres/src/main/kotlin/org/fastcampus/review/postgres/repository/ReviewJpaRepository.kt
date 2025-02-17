package org.fastcampus.review.postgres.repository

import org.fastcampus.review.postgres.entity.ReviewJpaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface ReviewJpaRepository : JpaRepository<ReviewJpaEntity, Long>, JpaSpecificationExecutor<ReviewJpaEntity> {
    fun findByOrderIdIn(orderIds: List<String>): List<ReviewJpaEntity>

    fun findByUserId(memberId: Long, pageable: Pageable): Page<ReviewJpaEntity>
}
