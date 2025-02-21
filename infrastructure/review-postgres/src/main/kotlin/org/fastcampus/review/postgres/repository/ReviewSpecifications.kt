package org.fastcampus.review.postgres.repository

import org.fastcampus.review.entity.Review
import org.fastcampus.review.postgres.entity.ReviewJpaEntity
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime

object ReviewSpecifications {
    fun hasStoreId(storeId: String): Specification<ReviewJpaEntity> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<String>("storeId"), storeId)
        }
    }

    fun createdAtBetween(startDateTime: LocalDateTime?, endDateTime: LocalDateTime?): Specification<ReviewJpaEntity>? {
        return if (startDateTime != null && endDateTime != null) {
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.between(root.get("createdAt"), startDateTime, endDateTime)
            }
        } else {
            null
        }
    }

    fun hasAnswerType(answerType: Review.AnswerType?): Specification<ReviewJpaEntity>? {
        return if (answerType == Review.AnswerType.OWNER_NOT_ANSWERED) {
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.isNull(root.get<Any>("adminUserId")) // adminUserId가 NULL인 경우만 필터링
            }
        } else {
            null
        }
    }
}
