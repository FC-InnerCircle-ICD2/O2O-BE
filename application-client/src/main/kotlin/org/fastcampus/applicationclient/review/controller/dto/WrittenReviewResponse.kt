package org.fastcampus.applicationclient.review.controller.dto

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class WrittenReviewResponse(
    val reviewId: Long,
    val storeId: String,
    val storeName: String,
    val createTime: LocalDateTime,
    val menuImage: String,
    val menuName: String,
    val totalScore: Int,
    val tasteScore: Int,
    val amountScore: Int,
    val representativeImageUri: String?,
    val clientReviewContent: String,
    val editDeadline: Int,
) {
    companion object {
        fun of(
            reviewId: Long,
            storeId: String?,
            storeName: String?,
            createTime: LocalDateTime,
            menuImage: String,
            menuName: String,
            totalScore: Int,
            tasteScore: Int,
            amountScore: Int,
            representativeImageUri: String?,
            clientReviewContent: String,
        ): WrittenReviewResponse {
            val daysPassed = ChronoUnit.DAYS.between(createTime, LocalDateTime.now()).toInt()

            return WrittenReviewResponse(
                reviewId = reviewId,
                storeId = storeId ?: "",
                storeName = storeName ?: "",
                createTime = createTime,
                menuImage = menuImage,
                menuName = menuName,
                totalScore = totalScore,
                tasteScore = tasteScore,
                amountScore = amountScore,
                representativeImageUri = representativeImageUri,
                clientReviewContent = clientReviewContent,
                editDeadline = maxOf(0, 3 - daysPassed),
            )
        }
    }
}
