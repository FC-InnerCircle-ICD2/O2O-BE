package org.fastcampus.applicationclient.review.service

import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.review.controller.dto.ReviewCreateRequest
import org.fastcampus.applicationclient.review.controller.dto.WritableReviewResponse
import org.fastcampus.applicationclient.review.controller.dto.WrittenReviewResponse
import org.fastcampus.common.dto.TimeBasedCursorDTO
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.review.repository.ReviewRepository
import org.fastcampus.store.repository.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
class ReviewService(
    private val reviewValidator: ReviewValidator,
    private val reviewImageUploader: ReviewImageUploader,
    private val reviewRepository: ReviewRepository,
    private val orderRepository: OrderRepository,
    private val storeRepository: StoreRepository,
) {
    @Transactional
    fun addReview(dto: ReviewCreateRequest, imageFile: MultipartFile?, user: AuthMember) {
        reviewValidator.validate(dto, user.id)

        if (imageFile != null) {
            val imageFullPath = dto.storeId + "/" + dto.orderId
            val imageUri = reviewImageUploader.upload(imageFullPath, imageFile)
            dto.representativeImageUri = imageUri
        }

        reviewRepository.save(dto.toModel(user.id))
    }

    @Transactional(readOnly = true)
    fun findWritableReview(user: AuthMember, cursor: LocalDateTime, size: Int): TimeBasedCursorDTO<WritableReviewResponse> {
        val orders = orderRepository.findReviewableOrders(user.id, cursor)
        val orderIds = orders.map { it.id }
        val reviewedOrderIds = reviewRepository.findByOrderIdIn(orderIds).map { it.orderId }.toSet()
        val writableOrders = orders
            .filter { it.id !in reviewedOrderIds }
            .sortedByDescending { it.orderTime }
            .take(size)

        val response = mutableListOf<WritableReviewResponse>()
        for (writableOrder in writableOrders) {
            val store = writableOrder.storeId?.let { storeRepository.findById(it) }
            if (store == null) continue
            response.add(
                WritableReviewResponse.of(
                    store.id,
                    store.name,
                    writableOrder.id,
                    writableOrder.orderSummary,
                    writableOrder.orderTime,
                ),
            )
        }

        return TimeBasedCursorDTO(
            content = response,
            if (response.isNotEmpty()) response.last().orderTime else null,
        )
    }

    @Transactional(readOnly = true)
    fun findWrittenReview(user: AuthMember, cursor: LocalDateTime, size: Int): TimeBasedCursorDTO<WrittenReviewResponse> {
        val findReviews = reviewRepository.findByUserId(user.id)

        val response = mutableListOf<WrittenReviewResponse>()
        for (review in findReviews) {
            val store = review.storeId.let { storeRepository.findById(it) }
            if (store == null) continue
            val order = review.orderId.let { orderRepository.findById(it) }
            if (order == null) continue
            response.add(
                WrittenReviewResponse.of(
                    store.id,
                    store.name,
                    requireNotNull(review.createdAt),
                    requireNotNull(store.imageMain),
                    requireNotNull(order.orderSummary),
                    review.totalScore,
                    review.tasteScore,
                    review.amountScore,
                    requireNotNull(review.representativeImageUri),
                    review.clientReviewContent,
                ),
            )
        }

        return TimeBasedCursorDTO(
            content = response,
            if (response.isNotEmpty()) response.last().createTime else null,
        )
    }
}
