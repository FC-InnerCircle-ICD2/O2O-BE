package org.fastcampus.applicationclient.review.controller

import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.review.controller.dto.ReviewCreateRequest
import org.fastcampus.applicationclient.review.service.ReviewService
import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/review")
class ReviewController(
    private val reviewService: ReviewService,
) {
    @PostMapping
    fun createReview(
        @RequestPart("review") dto: ReviewCreateRequest,
        @RequestPart("image", required = false) imageFile: MultipartFile?,
        @AuthenticationPrincipal user: AuthMember,
    ): APIResponseDTO<Nothing?> {
        reviewService.addReview(dto, imageFile, user)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, null)
    }
}
