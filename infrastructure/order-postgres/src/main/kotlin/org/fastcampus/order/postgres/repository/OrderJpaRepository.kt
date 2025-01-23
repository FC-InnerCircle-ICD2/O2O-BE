package org.fastcampus.order.postgres.repository

import org.fastcampus.order.entity.Order
import org.fastcampus.order.postgres.entity.OrderJpaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface OrderJpaRepository : JpaRepository<OrderJpaEntity, String> {
    fun findByUserId(userId: Long, pageable: Pageable): Page<OrderJpaEntity>

    fun findByStoreIdAndStatusAndOrderTimeBetween(
        storeId: String,
        status: Order.Status,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        pageable: Pageable,
    ): Page<OrderJpaEntity>
}
