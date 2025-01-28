package org.fastcampus.review.postgres.repository

import org.fastcampus.review.entity.Review
import org.fastcampus.review.postgres.entity.toJpaEntity
import org.fastcampus.review.postgres.entity.toModel
import org.fastcampus.review.repository.ReviewRepository
import org.springframework.stereotype.Repository

@Repository
class ReviewRepositoryCustom(
    private val reviewJpaRepository: ReviewJpaRepository,
) : ReviewRepository {
    override fun save(review: Review): Review {
        return reviewJpaRepository.save(review.toJpaEntity()).toModel()
    }
}
