package org.fastcampus.order.postgres.repository

import org.fastcampus.order.postgres.entity.OrderMenuOptionJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderMenuOptionJpaRepository : JpaRepository<OrderMenuOptionJpaEntity, Long> {
    fun findByOrderMenuOptionGroupId(orderMenuOptionGroupId: Long): List<OrderMenuOptionJpaEntity>
}
