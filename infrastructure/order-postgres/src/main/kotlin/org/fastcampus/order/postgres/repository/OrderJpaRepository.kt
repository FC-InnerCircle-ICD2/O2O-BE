package org.fastcampus.order.postgres.repository

import org.fastcampus.order.entity.Order
import org.fastcampus.order.postgres.entity.OrderJpaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface OrderJpaRepository : JpaRepository<OrderJpaEntity, String> {
    fun findByUserId(userId: Long, pageable: Pageable): Page<OrderJpaEntity>

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
    AND o.orderTime > :cursor
    AND o.orderTime <= :today
    ORDER BY o.orderTime DESC
""",
    )
    fun findByUserIdAndOrderTimeAfter(userId: Long, cursor: LocalDateTime, today: LocalDateTime): List<OrderJpaEntity>
}
