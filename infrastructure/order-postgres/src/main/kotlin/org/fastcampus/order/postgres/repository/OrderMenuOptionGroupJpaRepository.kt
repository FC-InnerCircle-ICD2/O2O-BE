package org.fastcampus.order.postgres.repository

import org.fastcampus.order.postgres.entity.OrderMenuOptionGroupJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderMenuOptionGroupJpaRepository : JpaRepository<OrderMenuOptionGroupJpaEntity, Long> {
    fun findByOrderMenuId(orderMenuId: Long): List<OrderMenuOptionGroupJpaEntity>
}
