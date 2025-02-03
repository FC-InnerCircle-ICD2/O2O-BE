package org.fastcampus.review.exception

open class ReviewException(message: String) : RuntimeException(message) {
    data class NotMatchedUser(val userId: Long) : ReviewException("리뷰 등록 요청이 불가능한 유저입니다. userId: $userId")

    data class ContentLength(val content: String) : ReviewException("리뷰는 5자 이상이어야 합니다., content: $content")

    data class InvalidScore(val scores: List<Int>) : ReviewException("점수는 0 ~ 5점 사이어야합니다., scores: $scores")

    data class ImageUploadFail(val imageUri: String) : ReviewException("이미지 업로드에 실패했습니다. uri: $imageUri")
}
