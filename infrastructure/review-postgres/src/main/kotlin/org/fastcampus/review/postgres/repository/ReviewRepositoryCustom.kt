package org.fastcampus.review.postgres.repository

import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.review.entity.Review
import org.fastcampus.review.postgres.entity.ReviewJpaEntity
import org.fastcampus.review.postgres.entity.toJpaEntity
import org.fastcampus.review.postgres.entity.toModel
import org.fastcampus.review.repository.ReviewRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Repository
import java.time.LocalDate

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

    override fun findReviews(
        storeId: String,
        startDate: LocalDate?,
        endDate: LocalDate?,
        sort: Review.Sort,
        answerType: Review.AnswerType,
        page: Int,
        size: Int,
    ): CursorDTO<Review> {
        val pageSort = if (sort == Review.Sort.SCORE) {
            Sort.by("totalScore").descending().and(Sort.by("createdAt").descending())
        } else {
            Sort.by("createdAt").descending()
        }
        val pageable = PageRequest.of(page, size, pageSort)

        val spec: Specification<ReviewJpaEntity> = Specification.where(ReviewSpecifications.hasStoreId(storeId))
            .and(ReviewSpecifications.createdAtBetween(startDate?.atStartOfDay(), endDate?.atTime(23, 59, 59)))
            .and(ReviewSpecifications.hasAnswerType(answerType))

        val reviews: Page<ReviewJpaEntity> = reviewJpaRepository.findAll(spec, pageable)
        return CursorDTO(
            content = reviews.content.map { it.toModel() },
            nextCursor = if (reviews.nextPageable().sort.isSorted) reviews.nextPageable().pageNumber else null,
        )
    }

    override fun getTotalAverageScoreByStoreId(storeId: String): Double {
        return reviewJpaRepository.getTotalAverageScoreByStoreId(storeId) ?: 0.0
    }

    override fun getTasteAverageScoreByStoreId(storeId: String): Double {
        return reviewJpaRepository.getTasteAverageScoreByStoreId(storeId) ?: 0.0
    }

    override fun getAmountAverageScoreByStoreId(storeId: String): Double {
        return reviewJpaRepository.getAmountAverageScoreByStoreId(storeId) ?: 0.0
    }

    override fun countReviewCountByStoreId(storeId: String): Long {
        return reviewJpaRepository.countByStoreId(storeId)
    }
}
