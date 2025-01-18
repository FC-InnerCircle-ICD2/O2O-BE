package org.fastcampus.order.postgres.repository

import org.fastcampus.order.entity.OrderMenuOption
import org.fastcampus.order.postgres.entity.toModel
import org.fastcampus.order.repository.OrderMenuOptionRepository
import org.springframework.stereotype.Repository

@Repository
class OrderMenuOptionJpaRepositoryCustom(
    private val orderMenuOptionJpaRepository: OrderMenuOptionJpaRepository,
) : OrderMenuOptionRepository {
    override fun findByOrderMenuOptionGroupId(orderMenuOptionGroupId: Long): List<OrderMenuOption> {
        return orderMenuOptionJpaRepository.findByOrderMenuOptionGroupId(orderMenuOptionGroupId).map { it.toModel() }
    }
}
