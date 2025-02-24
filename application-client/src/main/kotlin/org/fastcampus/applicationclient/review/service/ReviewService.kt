package org.fastcampus.applicationclient.review.service

import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.review.controller.dto.ReviewCreateRequest
import org.fastcampus.applicationclient.review.controller.dto.ReviewUpdateRequest
import org.fastcampus.applicationclient.review.controller.dto.WritableReviewResponse
import org.fastcampus.applicationclient.review.controller.dto.WrittenReviewResponse
import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.order.exception.OrderException
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.review.exception.ReviewException
import org.fastcampus.review.repository.ReviewRepository
import org.fastcampus.store.repository.StoreRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class ReviewService(
    private val reviewImageManager: ReviewImageManager,
    private val reviewRepository: ReviewRepository,
    private val orderRepository: OrderRepository,
    private val storeRepository: StoreRepository,
) {
    @Transactional
    fun addReview(dto: ReviewCreateRequest, imageFile: MultipartFile?, user: AuthMember) {
        // 주문정보 확인
        val order = orderRepository.findById(dto.orderId) ?: throw OrderException.OrderNotFound(dto.orderId)
        if (requireNotNull(order.userId) != user.id) {
            log.error("user who ordered doesn't match by request user id. userId in order: $order.userId, userId in request: ${user.id}")
            throw ReviewException.NotMatchedUser(user.id)
        }

        // 이미지 파일이 존재한다면 S3에 이미지를 전달 후, uri를 받아옴
        var imageUri: String? = null
        if (imageFile != null) {
            val imageFullPath = dto.storeId + "/" + dto.orderId
            imageUri = reviewImageManager.upload(imageFullPath, imageFile)
        }

        val review = reviewRepository.save(dto.toModel(user.id, imageUri))
        log.info("review has been created. reviewId = $review.id")
    }

    @Transactional(readOnly = true)
    fun findWritableReview(user: AuthMember): List<WritableReviewResponse> {
        val orders = orderRepository.findReviewableOrders(user.id)
        val orderIds = orders.map { it.id }
        val reviewedOrderIds = reviewRepository.findByOrderIdIn(orderIds).map { it.orderId }.toSet()
        val reviewableOrders = orders
            .filter { it.id !in reviewedOrderIds }
            .sortedByDescending { it.orderTime }

        val response = mutableListOf<WritableReviewResponse>()
        for (reviewableOrder in reviewableOrders) {
            val store = reviewableOrder.storeId?.let { storeRepository.findById(it) }
            if (store == null) continue
            response.add(
                WritableReviewResponse.of(
                    store.id,
                    store.name,
                    store.imageThumbnail,
                    reviewableOrder.id,
                    reviewableOrder.orderSummary,
                    reviewableOrder.orderTime,
                ),
            )
        }

        return response
    }

    @Transactional(readOnly = true)
    fun findWrittenReview(user: AuthMember, page: Int, size: Int): CursorDTO<WrittenReviewResponse> {
        val findReviews = reviewRepository.findByUserId(user.id, page, size)
        return CursorDTO(
            content = findReviews.content.map { review ->
                val store = review.storeId.let { storeRepository.findById(it) }
                val order = review.orderId.let { orderRepository.findById(it) }
                WrittenReviewResponse.of(
                    reviewId = requireNotNull(review.id),
                    storeId = store?.id,
                    storeName = store?.name,
                    createTime = requireNotNull(review.createdAt),
                    menuImage = requireNotNull(store?.imageMain),
                    menuName = requireNotNull(order?.orderSummary),
                    totalScore = review.totalScore,
                    tasteScore = review.tasteScore,
                    amountScore = review.amountScore,
                    deliveryQuality = requireNotNull(review.deliveryQuality),
                    representativeImageUri = review.representativeImageUri,
                    clientReviewContent = requireNotNull(review.clientReviewContent),
                )
            },
            nextCursor = findReviews.nextCursor?.plus(1),
            totalCount = findReviews.totalCount,
        )
    }

    @Transactional
    fun updateReview(reviewId: Long, dto: ReviewUpdateRequest, imageFile: MultipartFile?, user: AuthMember) {
        // 리뷰 정보 확인
        val review = reviewRepository.findById(reviewId)
        if (review.userId != user.id) {
            throw ReviewException.NotFoundReview(reviewId)
        }

        // 기존에 존재하던 이미지 수정
        if (dto.isImageChanged && imageFile != null) {
            val imageFullPath = review.storeId + "/" + review.orderId
            val imageUri = reviewImageManager.upload(imageFullPath, imageFile)
            review.changeRepresentativeImageUri(imageUri)
            log.info("image has been updated. reviewId = $review.id")
        }

        // 기존에 존재하던 이미지 수정
        if (dto.isImageChanged && imageFile == null) {
            review.representativeImageUri?.let {
                reviewImageManager.deleteImage(it)
                review.changeRepresentativeImageUri(null)
            }
            log.info("image has been deleted. reviewId = $review.id")
        }

        review.update(
            content = dto.content,
            totalScore = dto.totalScore,
            tasteScore = dto.tasteScore,
            amountScore = dto.amountScore,
            deliveryQuality = dto.deliveryQuality,
        )
        reviewRepository.save(review)
        log.info("review has been updated. reviewId = $review.id")
    }

    fun deleteReview(reviewId: Long, user: AuthMember) {
        // 리뷰 정보 확인
        val review = reviewRepository.findById(reviewId)
        if (review.userId != user.id) {
            throw ReviewException.NotFoundReview(reviewId)
        }

        // 이미지가 존재하면 삭제
        review.representativeImageUri?.let {
            reviewImageManager.deleteImage(it)
        }

        reviewRepository.delete(review)
        log.info("review has been deleted. reviewId = $review.id")
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}
