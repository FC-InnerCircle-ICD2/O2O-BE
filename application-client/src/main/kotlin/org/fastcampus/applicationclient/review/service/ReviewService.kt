package org.fastcampus.applicationclient.review.service

import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.review.controller.dto.ReviewCreateRequest
import org.fastcampus.applicationclient.review.controller.dto.WritableReviewResponse
import org.fastcampus.applicationclient.review.controller.dto.WrittenReviewResponse
import org.fastcampus.common.dto.CursorDTO
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

        // 이미지 파일이 존재한다면 S3에 이미지를 전달 후, uri를 받아옴
        var imageUri: String? = null
        if (imageFile != null) {
            val imageFullPath = dto.storeId + "/" + dto.orderId
            imageUri = reviewImageUploader.upload(imageFullPath, imageFile)
        }

        reviewRepository.save(dto.toModel(user.id, imageUri))
    }

    @Transactional(readOnly = true)
    fun findWritableReview(user: AuthMember, cursor: LocalDateTime, size: Int): TimeBasedCursorDTO<WritableReviewResponse> {
        val orders = orderRepository.findReviewableOrders(user.id, cursor)
        val orderIds = orders.map { it.id }
        val reviewedOrderIds = reviewRepository.findByOrderIdIn(orderIds).map { it.orderId }.toSet()
        val reviewableOrders = orders
            .filter { it.id !in reviewedOrderIds }
            .sortedByDescending { it.orderTime }
            .take(size)

        val response = mutableListOf<WritableReviewResponse>()
        for (reviewableOrder in reviewableOrders) {
            val store = reviewableOrder.storeId?.let { storeRepository.findById(it) }
            if (store == null) continue
            response.add(
                WritableReviewResponse.of(
                    store.id,
                    store.name,
                    reviewableOrder.id,
                    reviewableOrder.orderSummary,
                    reviewableOrder.orderTime,
                ),
            )
        }

        return TimeBasedCursorDTO(
            content = response,
            nextCursor = if (response.isNotEmpty()) response.last().orderTime else null,
        )
    }

    @Transactional(readOnly = true)
    fun findWrittenReview(user: AuthMember, page: Int, size: Int): CursorDTO<WrittenReviewResponse> {
        val findReviews = reviewRepository.findByUserId(user.id, page, size)
        return CursorDTO(
            content = findReviews.content.map { review ->
                val store = review.storeId.let { storeRepository.findById(it) }
                val order = review.orderId.let { orderRepository.findById(it) }
                WrittenReviewResponse.of(
                    storeId = store?.id,
                    storeName = store?.name,
                    createTime = requireNotNull(review.createdAt),
                    menuImage = requireNotNull(store?.imageMain),
                    menuName = requireNotNull(order?.orderSummary),
                    totalScore = review.totalScore,
                    tasteScore = review.tasteScore,
                    amountScore = review.amountScore,
                    representativeImageUri = requireNotNull(review.representativeImageUri),
                    clientReviewContent = requireNotNull(review.clientReviewContent),
                )
            },
            nextCursor = findReviews.nextCursor?.plus(1),
        )
    }
}
