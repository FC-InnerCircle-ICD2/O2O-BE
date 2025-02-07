package org.fastcampus.applicationclient.review.controller

import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.config.security.dto.JwtAuthenticated
import org.fastcampus.applicationclient.review.controller.dto.ReviewCreateRequest
import org.fastcampus.applicationclient.review.controller.dto.WritableReviewResponse
import org.fastcampus.applicationclient.review.controller.dto.WrittenReviewResponse
import org.fastcampus.applicationclient.review.service.ReviewService
import org.fastcampus.common.dto.APIResponseDTO
import org.fastcampus.common.dto.TimeBasedCursorDTO
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/reviews")
class ReviewController(
    private val reviewService: ReviewService,
) {
    @JwtAuthenticated
    @PostMapping
    fun createReview(
        @RequestPart("review") dto: ReviewCreateRequest,
        @RequestPart("image", required = false) imageFile: MultipartFile?,
        @AuthenticationPrincipal user: AuthMember,
    ): APIResponseDTO<Nothing?> {
        reviewService.addReview(dto, imageFile, user)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, null)
    }

    @JwtAuthenticated
    @GetMapping("/reviewable")
    fun gerReviewableOrder(
        @AuthenticationPrincipal user: AuthMember,
        @RequestParam("cursor") cursor: LocalDateTime,
        @RequestParam("size") size: Int,
    ): APIResponseDTO<TimeBasedCursorDTO<WritableReviewResponse>> {
        val findWritableReviews = reviewService.findWritableReview(user, cursor, size)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, findWritableReviews)
    }

    @JwtAuthenticated
    @GetMapping
    fun getWrittenReview(
        @AuthenticationPrincipal user: AuthMember,
        @RequestParam("cursor") cursor: LocalDateTime,
        @RequestParam("size") size: Int,
    ): APIResponseDTO<TimeBasedCursorDTO<WrittenReviewResponse>> {
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, reviewService.findWrittenReview(user, cursor, size))
    }
}
