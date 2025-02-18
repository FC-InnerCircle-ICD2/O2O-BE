package org.fastcampus.review.repository

import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.review.entity.Review
import java.time.LocalDate

interface ReviewRepository {
    fun save(review: Review): Review

    fun findByOrderIdIn(orderIds: List<String>): List<Review>

    fun findByUserId(memberId: Long, page: Int, size: Int): CursorDTO<Review>

    fun findReviews(
        storeId: String,
        startDate: LocalDate?,
        endDate: LocalDate?,
        sort: Review.Sort,
        answerType: Review.AnswerType,
        page: Int,
        size: Int,
    ): CursorDTO<Review>

    fun findById(reviewId: Long): Review

    fun getTotalAverageScoreByStoreId(storeId: String): Double

    fun getTasteAverageScoreByStoreId(storeId: String): Double

    fun getAmountAverageScoreByStoreId(storeId: String): Double

    fun countReviewCountByStoreId(storeId: String): Long
}
