package org.fastcampus.order.postgres.repository

import org.fastcampus.order.postgres.entity.OrderJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderJpaRepository : JpaRepository<OrderJpaEntity, Long>
