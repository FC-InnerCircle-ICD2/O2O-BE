package org.fastcampus.order.postgres.repository

import org.fastcampus.order.postgres.entity.OrderMenuJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderMenuJpaRepository : JpaRepository<OrderMenuJpaEntity, Long> {
    fun findByOrderId(orderId: String): List<OrderMenuJpaEntity>
}
