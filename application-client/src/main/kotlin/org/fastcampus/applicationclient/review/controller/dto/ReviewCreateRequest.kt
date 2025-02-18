package org.fastcampus.applicationclient.review.controller.dto

import org.fastcampus.review.entity.Review
import org.fastcampus.review.entity.Review.DeliveryQuality

data class ReviewCreateRequest(
    val orderId: String,
    val storeId: String,
    val content: String,
    val totalScore: Int,
    val tasteScore: Int,
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
