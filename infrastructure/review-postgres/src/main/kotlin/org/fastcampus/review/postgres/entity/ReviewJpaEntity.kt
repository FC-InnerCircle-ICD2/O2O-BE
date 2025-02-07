package org.fastcampus.review.postgres.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fastcampus.common.entity.BaseEntity
import org.fastcampus.review.entity.Review
import org.fastcampus.review.entity.Review.DeliveryQuality

@Entity
@Table(name = "REVIEWS")
class ReviewJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "ORDER_ID", nullable = false, length = 255)
    val orderId: String,
    @Column(name = "USER_ID", nullable = false)
    val userId: Long,
    @Column(nullable = false)
    val storeId: String,
    @Column(name = "CLIENT_REVIEW_CONTENT", nullable = false, length = 1000)
    val clientReviewContent: String,
    @Column(name = "TOTAL_SCORE", nullable = false)
    val totalScore: Int,
    @Column(name = "TASTE_SCORE", nullable = false)
    val tasteScore: Int,
    @Column(name = "AMOUNT_SCORE", nullable = false)
    val amountScore: Int,
    @Column(name = "REPRESENTATIVE_IMAGE_URI", nullable = true, length = 500)
    val representativeImageUri: String? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "DELIVERY_QUALITY", nullable = true, length = 50)
    val deliveryQuality: DeliveryQuality? = null,
    @Column(name = "ADMIN_USER_ID", nullable = true)
    val adminUserId: Long? = null,
    @Column(name = "ADMIN_REVIEW_CONTENT", nullable = true, length = 1000)
    val adminReviewContent: String? = null,
) : BaseEntity()

fun Review.toJpaEntity() =
    ReviewJpaEntity(
        id = this.id,
        orderId = this.orderId,
        userId = this.userId,
        storeId = this.storeId,
        clientReviewContent = this.clientReviewContent,
        totalScore = this.totalScore,
        tasteScore = this.tasteScore,
        amountScore = this.amountScore,
        representativeImageUri = this.representativeImageUri,
        deliveryQuality = this.deliveryQuality,
        adminUserId = this.adminUserId,
        adminReviewContent = this.adminReviewContent,
    )

fun ReviewJpaEntity.toModel() =
    Review(
        id = this.id,
        orderId = this.orderId,
        userId = this.userId,
        storeId = this.storeId,
        clientReviewContent = this.clientReviewContent,
        totalScore = this.totalScore,
        tasteScore = this.tasteScore,
        amountScore = this.amountScore,
        representativeImageUri = this.representativeImageUri,
        deliveryQuality = this.deliveryQuality,
        adminUserId = this.adminUserId,
        adminReviewContent = this.adminReviewContent,
    )
