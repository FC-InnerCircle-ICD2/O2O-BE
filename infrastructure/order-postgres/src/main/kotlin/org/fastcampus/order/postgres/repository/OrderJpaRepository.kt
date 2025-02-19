package org.fastcampus.order.postgres.repository

import org.fastcampus.order.entity.Order
import org.fastcampus.order.postgres.entity.OrderJpaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface OrderJpaRepository : JpaRepository<OrderJpaEntity, String>, JpaSpecificationExecutor<OrderJpaEntity> {
    @Query(
        """
        SELECT o FROM OrderJpaEntity o
        WHERE o.userId = :userId
        AND o.status <> :status
        AND (:keyword IS NULL OR :keyword = ''
            OR o.orderSummary LIKE %:keyword%
            OR o.storeName LIKE %:keyword%)
    """,
    )
    fun findByUserIdAndStatusNot(userId: Long, keyword: String, status: Order.Status, pageable: Pageable): Page<OrderJpaEntity>

    @Query(
        """
        SELECT o FROM OrderJpaEntity o
        WHERE o.userId = :userId
        AND o.orderTime >= :orderTime
        AND o.orderTime < :cursor
    """,
    )
    fun findByUserIdAndOrderTimeAfterWithCursor(userId: Long, orderTime: LocalDateTime, cursor: LocalDateTime): List<OrderJpaEntity>

    @Query(
        """
        SELECT o
        FROM OrderJpaEntity o
        WHERE o.storeId = :storeId
          AND o.orderTime BETWEEN :startDateTime AND :endDateTime
          AND o.status IN :includedStatus
    """,
    )
    fun findAllByStoreIdAndOrderTimeBetweenAndStatusIn(
        @Param("storeId") storeId: String,
        @Param("startDateTime") startDateTime: LocalDateTime,
        @Param("endDateTime") endDateTime: LocalDateTime,
        @Param("includedStatus") includedStatus: List<Order.Status>,
    ): List<OrderJpaEntity>
}
