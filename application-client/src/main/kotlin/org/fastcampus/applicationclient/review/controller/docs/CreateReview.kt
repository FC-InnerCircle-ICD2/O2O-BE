package org.fastcampus.applicationclient.review.controller.docs

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.review.controller.dto.ReviewCreateRequest
import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

interface CreateReview {
    @Operation(
        summary = "리뷰 생성",
        description = "리뷰를 생성하고, 선택적으로 이미지를 업로드합니다.",
    )
    fun createReview(
        @RequestPart("review")
        @RequestBody(
            description = "리뷰 생성 요청 데이터",
            required = true,
            content = [Content(schema = Schema(implementation = ReviewCreateRequest::class))],
        )
        dto: ReviewCreateRequest,
        @RequestPart("image", required = false)
        @RequestBody(
            description = "업로드할 이미지 파일 (선택 사항)",
            required = false,
            content = [Content(mediaType = "image/*")],
        )
        imageFile: MultipartFile?,
        @AuthenticationPrincipal
        user: AuthMember,
    ): APIResponseDTO<Nothing?>
}
