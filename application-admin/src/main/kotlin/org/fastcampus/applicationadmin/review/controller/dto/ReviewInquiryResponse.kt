package org.fastcampus.applicationadmin.review.controller.dto

import com.fasterxml.jackson.annotation.JsonFormat
import org.fastcampus.member.entity.Member
import org.fastcampus.order.entity.Order
import org.fastcampus.review.entity.Review
import java.time.LocalDate

data class ReviewInquiryResponse(
    val id: Long,
    val nickname: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val date: LocalDate,
    val rating: ScoreResponse,
    val menu: List<MenuResponse>,
    val content: String,
    val images: List<String?>,
    val reply: OwnerReplyResponse?,
) {
    companion object {
        fun of(review: Review, order: Order, user: Member): ReviewInquiryResponse {
            return ReviewInquiryResponse(
                id = review.id!!,
                nickname = user.nickname,
                date = review.createdAt!!.toLocalDate(),
                rating = ScoreResponse(review.totalScore, review.amountScore, review.tasteScore),
                menu = order.orderMenus?.map { MenuResponse.of(it) } ?: emptyList(),
                content = review.clientReviewContent,
                images = listOf(review.representativeImageUri),
                reply = review.adminUserId?.let {
                    OwnerReplyResponse(review.adminReviewedAt?.toLocalDate() ?: LocalDate.now(), review.adminReviewContent ?: "")
                },
            )
        }
    }
}
