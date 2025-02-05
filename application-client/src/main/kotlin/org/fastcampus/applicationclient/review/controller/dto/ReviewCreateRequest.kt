package org.fastcampus.applicationclient.review.controller.dto

import org.fastcampus.review.entity.Review
import org.fastcampus.review.entity.Review.DeliveryQuality

data class ReviewCreateRequest(
    val orderId: String,
    val storeId: String,
    val content: String,
    val totalScore: Int,
    val tasteScore: Int,
    val quantityScore: Int,
    val deliveryQuality: DeliveryQuality? = null,
    var representativeImageUri: String? = null,
) {
    fun toModel(userId: Long): Review {
        return Review(
            orderId = orderId,
            userId = userId,
            storeId = storeId,
            clientReviewContent = content,
            totalScore = totalScore,
            tasteScore = tasteScore,
            quantityScore = quantityScore,
            deliveryQuality = deliveryQuality,
            representativeImageUri = representativeImageUri,
        )
    }
}
