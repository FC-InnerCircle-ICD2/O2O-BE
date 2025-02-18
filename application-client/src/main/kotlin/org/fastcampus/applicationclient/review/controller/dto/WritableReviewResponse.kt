package org.fastcampus.applicationclient.review.controller.dto

import java.time.LocalDateTime

data class WritableReviewResponse(
    val storeId: String?,
    val storeName: String?,
    val storeImageThumbnail: String?,
    val orderId: String?,
    val orderSummary: String?,
    val orderTime: LocalDateTime?,
) {
    companion object {
        fun of(
            storeId: String?,
            storeName: String?,
            storeImageThumbnail: String?,
            orderId: String?,
            orderSummary: String?,
            orderTime: LocalDateTime?,
        ): WritableReviewResponse {
            return WritableReviewResponse(
                storeId = storeId ?: "",
                storeName = storeName ?: "",
                storeImageThumbnail = storeImageThumbnail ?: "",
                orderId = orderId ?: "",
                orderSummary = orderSummary ?: "",
                orderTime = orderTime,
            )
        }
    }
}
