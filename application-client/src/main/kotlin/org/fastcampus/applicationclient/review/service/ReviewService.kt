package org.fastcampus.applicationclient.review.service

import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.review.controller.dto.ReviewCreateRequest
import org.fastcampus.review.repository.ReviewRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class ReviewService(
    private val reviewValidator: ReviewValidator,
    private val reviewImageUploader: ReviewImageUploader,
    private val reviewRepository: ReviewRepository,
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
}
