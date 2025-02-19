package org.fastcampus.applicationclient.review.controller.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.fastcampus.review.entity.Review
import org.fastcampus.review.entity.Review.DeliveryQuality

data class ReviewCreateRequest(
    @field:NotBlank(message = "주문 ID는 필수입니다.")
    val orderId: String,
    @field:NotBlank(message = "가게 ID는 필수입니다.")
    val storeId: String,
    @field:NotBlank(message = "리뷰 내용은 필수입니다.")
    val content: String,
    @field:Min(0) @field:Max(5)
    val totalScore: Int,
    @field:Min(0) @field:Max(5)
    val tasteScore: Int,
    @field:Min(0) @field:Max(5)
    val amountScore: Int,
    val deliveryQuality: DeliveryQuality? = null,
) {
    fun toModel(userId: Long, imageUri: String? = null): Review {
        return Review(
            orderId = orderId,
            userId = userId,
            storeId = storeId,
            clientReviewContent = content,
            totalScore = totalScore,
            tasteScore = tasteScore,
            amountScore = amountScore,
            deliveryQuality = deliveryQuality,
            representativeImageUri = imageUri,
        )
    }
}
