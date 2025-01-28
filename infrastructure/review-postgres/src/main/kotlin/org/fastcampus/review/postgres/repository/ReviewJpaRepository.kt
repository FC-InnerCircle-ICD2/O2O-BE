package org.fastcampus.review.postgres.repository

import org.fastcampus.review.postgres.entity.ReviewJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewJpaRepository : JpaRepository<ReviewJpaEntity, Long>
