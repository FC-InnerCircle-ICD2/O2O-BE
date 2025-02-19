package org.fastcampus.applicationadmin.review.controller

import org.fastcampus.applicationadmin.config.security.dto.AuthMember
import org.fastcampus.applicationadmin.review.controller.dto.ReviewInquiryResponse
import org.fastcampus.applicationadmin.review.controller.dto.ReviewReplyRequest
import org.fastcampus.applicationadmin.review.controller.dto.SummaryResponse
import org.fastcampus.applicationadmin.review.service.ReviewService
import org.fastcampus.common.dto.APIResponseDTO
import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.review.entity.Review
import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/reviews")
class ReviewController(
    private val reviewService: ReviewService,
) {
    @GetMapping
    fun getReviews(
        @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") startDate: LocalDate?,
        @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") endDate: LocalDate?,
        @RequestParam sort: Review.Sort,
        @RequestParam answerType: Review.AnswerType,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int,
        @AuthenticationPrincipal owner: AuthMember,
    ): APIResponseDTO<CursorDTO<ReviewInquiryResponse>> {
        log.debug("admin tried to inquire reviews: owner= {}", owner)
        return APIResponseDTO(
            HttpStatus.OK.value(),
            HttpStatus.OK.reasonPhrase,
            reviewService.findReviews(owner.id, startDate, endDate, sort, answerType, page, size),
        )
    }

    @PostMapping("/{reviewId}/reply")
    fun replyReview(
        @PathVariable reviewId: Long,
        @AuthenticationPrincipal owner: AuthMember,
        @RequestBody requestDto: ReviewReplyRequest,
    ): APIResponseDTO<String> {
        log.debug("reply review: ownerId= {}, reviewId= {}", owner, reviewId)
        reviewService.replyReview(reviewId, owner, requestDto)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, null)
    }

    @DeleteMapping("/{reviewId}/reply")
    fun deleteReply(
        @PathVariable reviewId: Long,
        @AuthenticationPrincipal owner: AuthMember,
    ): APIResponseDTO<String> {
        log.debug("delete reply: ownerId= {}, reviewId= {}", owner, reviewId)
        reviewService.deleteReply(reviewId, owner)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, null)
    }

    @GetMapping("/summary")
    fun getSummary(
        @AuthenticationPrincipal owner: AuthMember,
    ): APIResponseDTO<SummaryResponse> {
        val response = reviewService.getSummary(owner.id)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, response)
    }

    companion object {
        private val log = LoggerFactory.getLogger(ReviewController::class.java)
    }
}
