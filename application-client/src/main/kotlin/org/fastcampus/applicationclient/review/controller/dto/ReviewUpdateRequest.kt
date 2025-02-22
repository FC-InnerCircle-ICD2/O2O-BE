package org.fastcampus.applicationclient.review.controller.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.fastcampus.review.entity.Review.DeliveryQuality

class ReviewUpdateRequest(
    @field:NotBlank(message = "리뷰 내용은 비어 있을 수 없습니다.")
    val content: String,
    @field:Min(0) @field:Max(5)
    val totalScore: Int,
    @field:Min(0) @field:Max(5)
    val tasteScore: Int,
    @field:Min(0) @field:Max(5)
    val amountScore: Int,
    @NotNull
    val deliveryQuality: DeliveryQuality,
    @NotNull
    val isImageChanged: Boolean,
)
