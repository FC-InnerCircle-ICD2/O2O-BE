package org.fastcampus.order.postgres.repository

import org.fastcampus.order.entity.Order
import org.fastcampus.order.postgres.entity.OrderJpaEntity
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime

object OrderSpecifications {
    fun hasStoreId(storeId: String): Specification<OrderJpaEntity> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<String>("storeId"), storeId)
        }
    }

    fun hasStatus(statusList: List<Order.Status>): Specification<OrderJpaEntity> {
        return Specification { root, _, _ ->
            root.get<Order.Status>("status").`in`(statusList)
        }
    }

    fun orderTimeBetween(startDateTime: LocalDateTime?, endDateTime: LocalDateTime?): Specification<OrderJpaEntity>? {
        return if (startDateTime != null && endDateTime != null) {
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.between(root.get("orderTime"), startDateTime, endDateTime)
            }
        } else {
            null
        }
    }
}
