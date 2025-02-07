package org.fastcampus.review.postgres.repository

import org.fastcampus.review.entity.Review
import org.fastcampus.review.postgres.entity.ReviewJpaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewJpaRepository : JpaRepository<ReviewJpaEntity, Long> {
    fun findByOrderIdIn(orderIds: List<String>): List<Review>

    fun findByUserId(memberId: Long, pageable: Pageable): Page<ReviewJpaEntity>
}
