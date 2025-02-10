package org.fastcampus.review.entity

import java.time.LocalDateTime

class Review(
    val id: Long? = null,
    val orderId: String,
    val userId: Long,
    val storeId: String,
    val clientReviewContent: String,
    val totalScore: Int,
    val tasteScore: Int,
    val amountScore: Int,
    val representativeImageUri: String? = null,
    val deliveryQuality: DeliveryQuality? = null,
    val adminUserId: Long? = null,
    val adminReviewContent: String? = null,
    val createdAt: LocalDateTime? = null,
) {
    enum class DeliveryQuality(
        val code: String,
        val desc: String,
    ) {
        GOOD("d1", "Good"),
        BAD("d2", "Bad"),
    }
}
