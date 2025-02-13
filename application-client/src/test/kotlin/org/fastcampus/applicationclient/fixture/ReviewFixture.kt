package org.fastcampus.applicationclient.fixture

import org.fastcampus.review.entity.Review
import java.time.LocalDateTime

fun createReview(
    id: Long? = 1L,
    orderId: String = "ORDER123",
    userId: Long = 1001L,
    storeId: String = "STORE456",
    clientReviewContent: String = "Great food!",
    totalScore: Int = 5,
    tasteScore: Int = 5,
    amountScore: Int = 4,
    representativeImageUri: String? = "https://example.com/image.jpg",
    deliveryQuality: Review.DeliveryQuality? = Review.DeliveryQuality.GOOD,
    adminUserId: Long? = null,
    adminReviewContent: String? = null,
    createdAt: LocalDateTime? = LocalDateTime.now(),
    updatedAt: LocalDateTime? = LocalDateTime.now(),
): Review {
    return Review(
        id = id,
        orderId = orderId,
        userId = userId,
        storeId = storeId,
        clientReviewContent = clientReviewContent,
        totalScore = totalScore,
        tasteScore = tasteScore,
        amountScore = amountScore,
        representativeImageUri = representativeImageUri,
        deliveryQuality = deliveryQuality,
        adminUserId = adminUserId,
        adminReviewContent = adminReviewContent,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
