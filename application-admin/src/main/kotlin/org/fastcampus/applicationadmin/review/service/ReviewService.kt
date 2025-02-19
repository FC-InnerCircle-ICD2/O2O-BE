package org.fastcampus.applicationadmin.review.service

import org.fastcampus.applicationadmin.config.security.dto.AuthMember
import org.fastcampus.applicationadmin.order.service.component.OrderReader
import org.fastcampus.applicationadmin.review.controller.dto.ReviewInquiryResponse
import org.fastcampus.applicationadmin.review.controller.dto.ReviewReplyRequest
import org.fastcampus.applicationadmin.review.controller.dto.SummaryResponse
import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.member.repository.MemberRepository
import org.fastcampus.review.entity.Review
import org.fastcampus.review.exception.ReviewException
import org.fastcampus.review.repository.ReviewRepository
import org.fastcampus.store.repository.StoreRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val storeRepository: StoreRepository,
    private val orderReader: OrderReader,
    private val memberRepository: MemberRepository,
) {
    fun findReviews(
        ownerId: Long,
        startDate: LocalDate?,
        endDate: LocalDate?,
        sort: Review.Sort,
        answerType: Review.AnswerType,
        page: Int,
        size: Int,
    ): CursorDTO<ReviewInquiryResponse> {
        val storeId = storeRepository.findByOwnerId(ownerId.toString())
        log.debug("review is inquired for ownerId: $ownerId")
        val reviews: CursorDTO<Review> = reviewRepository.findReviews(storeId, startDate, endDate, sort, answerType, page, size)

        val result = mutableListOf<ReviewInquiryResponse>()
        for (review in reviews.content) {
            val order = orderReader.findOrderByIdWithSubItems(review.orderId)
            val user = memberRepository.findById(order.userId!!)
            result.add(ReviewInquiryResponse.of(review, order, user))
        }
        return CursorDTO(content = result, nextCursor = reviews.nextCursor)
    }

    fun replyReview(reviewId: Long, owner: AuthMember, requestDto: ReviewReplyRequest) {
        val review = reviewRepository.findById(reviewId)
        val storeId = storeRepository.findByOwnerId(ownerId = owner.id.toString())
        if (review.storeId != storeId) {
            throw ReviewException.NotMatchedOwner(owner.id)
        }
        review.reply(owner.id, requestDto.content)
        reviewRepository.save(review)
    }

    fun deleteReply(reviewId: Long, owner: AuthMember) {
        val review = reviewRepository.findById(reviewId)
        val storeId = storeRepository.findByOwnerId(ownerId = owner.id.toString())
        if (review.storeId != storeId) {
            throw ReviewException.NotMatchedOwner(owner.id)
        }
        review.deleteReply()
        reviewRepository.save(review)
    }

    fun getSummary(ownerId: Long): SummaryResponse {
        val storeId = storeRepository.findByOwnerId(ownerId.toString())
        return SummaryResponse(
            totalRating = reviewRepository.getTotalAverageScoreByStoreId(storeId),
            quantityRating = reviewRepository.getAmountAverageScoreByStoreId(storeId),
            tasteRating = reviewRepository.getTasteAverageScoreByStoreId(storeId),
            reviewCount = reviewRepository.countReviewCountByStoreId(storeId),
        )
    }

    companion object {
        private val log = LoggerFactory.getLogger(ReviewService::class.java)
    }
}
