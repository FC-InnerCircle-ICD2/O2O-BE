package org.fastcampus.order.postgres.repository

import org.fastcampus.order.entity.OrderMenu
import org.fastcampus.order.postgres.entity.toModel
import org.fastcampus.order.repository.OrderMenuRepository
import org.springframework.stereotype.Repository

@Repository
class OrderMenuJpaRepositoryCustom(
    private val orderMenuJpaRepository: OrderMenuJpaRepository,
) : OrderMenuRepository {
    override fun findByOrderId(orderId: String): List<OrderMenu> {
        return orderMenuJpaRepository.findByOrderId(orderId)
            .map { it.toModel() }
    }
}
