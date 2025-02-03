package org.fastcampus.applicationclient.review.service

import org.fastcampus.applicationclient.review.controller.dto.ReviewCreateRequest
import org.fastcampus.order.exception.OrderException
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.review.exception.ReviewException
import org.springframework.stereotype.Component

@Component
class ReviewValidator(
    private val orderRepository: OrderRepository,
) {
    fun validate(dto: ReviewCreateRequest, userId: Long) {
        // 유저검증
        val order = orderRepository.findById(dto.orderId) ?: throw OrderException.OrderNotFound(dto.orderId)
        if (requireNotNull(order.userId) != userId) {
            throw ReviewException.NotMatchedUser(userId)
        }

        // 길이 검증
        if (dto.content.trim().length <= 5) {
            throw ReviewException.ContentLength(dto.content)
        }

        // 점수 범위 검증 (0~5점)
        val scores = listOf(dto.totalScore, dto.tasteScore, dto.quantityScore)
        if (scores.any { it < 0 || it > 5 }) {
            throw ReviewException.InvalidScore(scores) // 점수가 범위를 벗어날 때 예외
        }
    }
}
