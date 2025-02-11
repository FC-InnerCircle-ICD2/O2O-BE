package org.fastcampus.order.postgres.repository

import org.fastcampus.order.entity.Order
import org.fastcampus.order.postgres.entity.OrderJpaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface OrderJpaRepository : JpaRepository<OrderJpaEntity, String> {
    fun findByUserIdAndStatusNot(userId: Long, status: Order.Status, pageable: Pageable): Page<OrderJpaEntity>

    fun findByStoreIdAndStatusInAndOrderTimeBetween(
        storeId: String,
        status: List<Order.Status>,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        pageable: Pageable,
    ): Page<OrderJpaEntity>

    @Query(
        """
        SELECT o FROM OrderJpaEntity o
        WHERE o.userId = :userId
        AND o.orderTime >= :orderTime
        AND o.orderTime < :cursor
    """,
    )
    fun findByUserIdAndOrderTimeAfterWithCursor(userId: Long, orderTime: LocalDateTime, cursor: LocalDateTime): List<OrderJpaEntity>
}
