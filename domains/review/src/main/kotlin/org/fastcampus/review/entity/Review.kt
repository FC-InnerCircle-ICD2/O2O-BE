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
