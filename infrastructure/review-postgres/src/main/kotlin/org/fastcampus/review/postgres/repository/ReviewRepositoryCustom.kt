package org.fastcampus.review.postgres.repository

import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.review.entity.Review
import org.fastcampus.review.postgres.entity.toJpaEntity
import org.fastcampus.review.postgres.entity.toModel
import org.fastcampus.review.repository.ReviewRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository
class ReviewRepositoryCustom(
    private val reviewJpaRepository: ReviewJpaRepository,
) : ReviewRepository {
    override fun save(review: Review): Review {
        return reviewJpaRepository.save(review.toJpaEntity()).toModel()
    }

    override fun findByOrderIdIn(orderIds: List<String>): List<Review> {
        return reviewJpaRepository.findByOrderIdIn(orderIds).map { it.toModel() }
    }

    override fun findByUserId(memberId: Long, page: Int, size: Int): CursorDTO<Review> {
        val pageable: Pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        val findReviews = reviewJpaRepository.findByUserId(memberId, pageable)
        return CursorDTO(
            content = findReviews.content.map { it.toModel() },
            nextCursor = if (findReviews.nextPageable().sort.isSorted) findReviews.nextPageable().pageNumber else null,
        )
    }
}
