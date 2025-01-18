package org.fastcampus.order.postgres.repository

import org.fastcampus.order.postgres.entity.OrderJpaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface OrderJpaRepository : JpaRepository<OrderJpaEntity, String> {
    fun findByUserId(userId: Long, pageable: Pageable): Page<OrderJpaEntity>
}
