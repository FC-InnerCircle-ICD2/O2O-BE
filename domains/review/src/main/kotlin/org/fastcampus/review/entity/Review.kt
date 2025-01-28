package org.fastcampus.review.entity

class Review(
    val id: Long? = null,
    val orderId: String,
    val userId: Long,
    val storeId: String,
    val clientReviewContent: String,
    val totalScore: Int,
    val tasteScore: Int,
    val quantityScore: Int,
    val representativeImageUri: String? = null,
    val deliveryQuality: DeliveryQuality? = null,
    val adminUserId: Long? = null,
    val adminReviewContent: String? = null,
) {
    enum class DeliveryQuality(
        val code: String,
        val desc: String,
    ) {
        GOOD("d1", "Good"),
        BAD("d2", "Bad"),
    }
}
