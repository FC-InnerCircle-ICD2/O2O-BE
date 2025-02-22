package org.fastcampus.applicationclient.review.controller

import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.config.security.dto.JwtAuthenticated
import org.fastcampus.applicationclient.review.controller.docs.ReviewControllerDocs
import org.fastcampus.applicationclient.review.controller.dto.ReviewCreateRequest
import org.fastcampus.applicationclient.review.controller.dto.ReviewUpdateRequest
import org.fastcampus.applicationclient.review.controller.dto.WritableReviewResponse
import org.fastcampus.applicationclient.review.controller.dto.WrittenReviewResponse
import org.fastcampus.applicationclient.review.service.ReviewService
import org.fastcampus.common.dto.APIResponseDTO
import org.fastcampus.common.dto.CursorDTO
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/reviews")
class ReviewController(
    private val reviewService: ReviewService,
) : ReviewControllerDocs {
    @JwtAuthenticated
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun createReview(
        @RequestPart("review") dto: ReviewCreateRequest,
        @RequestPart("image", required = false) imageFile: MultipartFile?,
        @AuthenticationPrincipal user: AuthMember,
    ): APIResponseDTO<Nothing?> {
        reviewService.addReview(dto, imageFile, user)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, null)
    }

    @JwtAuthenticated
    @GetMapping("/writable")
    fun gerReviewableOrder(
        @AuthenticationPrincipal user: AuthMember,
    ): APIResponseDTO<List<WritableReviewResponse>> {
        val findWritableReviews = reviewService.findWritableReview(user)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, findWritableReviews)
    }

    @JwtAuthenticated
    @GetMapping
    fun getWrittenReview(
        @AuthenticationPrincipal user: AuthMember,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "5") size: Int,
    ): APIResponseDTO<CursorDTO<WrittenReviewResponse>> {
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, reviewService.findWrittenReview(user, page - 1, size))
    }

    @JwtAuthenticated
    @PatchMapping(path = ["/{reviewId}"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateReview(
        @PathVariable("reviewId") reviewId: Long,
        @RequestPart("review") dto: ReviewUpdateRequest,
        @RequestPart("image", required = false) imageFile: MultipartFile?,
        @AuthenticationPrincipal user: AuthMember,
    ): APIResponseDTO<Nothing?> {
        reviewService.updateReview(reviewId, dto, imageFile, user)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, null)
    }

    @JwtAuthenticated
    @DeleteMapping("/{reviewId}")
    fun deleteReview(
        @PathVariable("reviewId") reviewId: Long,
        @AuthenticationPrincipal user: AuthMember,
    ): APIResponseDTO<Nothing?> {
        reviewService.deleteReview(reviewId, user)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, null)
    }
}
