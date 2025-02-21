package org.fastcampus.applicationadmin.review.controller.dto

data class SummaryResponse(
    val totalRating: Double,
    val quantityRating: Double,
    val tasteRating: Double,
    val reviewCount: Long,
)
