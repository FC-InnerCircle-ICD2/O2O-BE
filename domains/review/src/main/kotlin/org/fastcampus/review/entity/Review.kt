package org.fastcampus.review.entity

import java.time.LocalDateTime

class Review(
    val id: Long? = null,
    val orderId: String,
    val userId: Long,
    val storeId: String,
    var clientReviewContent: String,
    var totalScore: Int,
    var tasteScore: Int,
    var amountScore: Int,
    var representativeImageUri: String? = null,
    var deliveryQuality: DeliveryQuality? = null,
    var adminUserId: Long? = null,
    var adminReviewContent: String? = null,
    var adminReviewedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
) {
    fun reply(ownerId: Long, content: String) {
        this.adminUserId = ownerId
        this.adminReviewContent = content
        this.updatedAt = LocalDateTime.now()
        this.adminReviewedAt = LocalDateTime.now()
    }

    fun deleteReply() {
        this.adminUserId = null
        this.adminReviewContent = null
        this.updatedAt = LocalDateTime.now()
    }

    fun update(
        content: String,
        totalScore: Int,
        tasteScore: Int,
        amountScore: Int,
        deliveryQuality: DeliveryQuality,
        representativeImageUri: String? = null,
    ) {
        this.clientReviewContent = content
        this.totalScore = totalScore
        this.tasteScore = tasteScore
        this.amountScore = amountScore
        this.deliveryQuality = deliveryQuality
        if (representativeImageUri != null) {
            this.representativeImageUri = representativeImageUri
        }
    }

    enum class DeliveryQuality(
        val code: String,
        val desc: String,
    ) {
        GOOD("d1", "Good"),
        BAD("d2", "Bad"),
    }

    enum class Sort(
        val code: String,
        val desc: String,
    ) {
        LATEST("s1", "latest"),
        SCORE("s2", "score"),
    }

    enum class AnswerType(
        val code: String,
        val desc: String,
    ) {
        ALL("a1", "all"),
        OWNER_ANSWERED("a2", "owner_answered"),
        OWNER_NOT_ANSWERED("a3", "owner_not_answered"),
    }
}
