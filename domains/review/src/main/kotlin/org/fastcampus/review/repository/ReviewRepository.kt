package org.fastcampus.review.repository

import org.fastcampus.review.entity.Review

interface ReviewRepository {
    fun save(review: Review): Review
}
