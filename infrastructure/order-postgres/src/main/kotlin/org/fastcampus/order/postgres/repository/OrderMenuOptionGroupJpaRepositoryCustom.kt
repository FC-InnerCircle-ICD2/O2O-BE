package org.fastcampus.order.postgres.repository

import org.fastcampus.order.entity.OrderMenuOptionGroup
import org.fastcampus.order.postgres.entity.toJpaEntity
import org.fastcampus.order.postgres.entity.toModel
import org.fastcampus.order.repository.OrderMenuOptionGroupRepository
import org.springframework.stereotype.Repository

@Repository
class OrderMenuOptionGroupJpaRepositoryCustom(
    private val orderMenuOptionGroupJpaRepository: OrderMenuOptionGroupJpaRepository,
) : OrderMenuOptionGroupRepository {
    override fun findByOrderMenuId(orderMenuId: Long): List<OrderMenuOptionGroup> {
        return orderMenuOptionGroupJpaRepository.findByOrderMenuId(orderMenuId)
            .map { it.toModel() }
    }

    override fun save(orderMenuOptionGroup: OrderMenuOptionGroup): OrderMenuOptionGroup {
        return orderMenuOptionGroupJpaRepository.saveAndFlush(orderMenuOptionGroup.toJpaEntity()).toModel()
    }
}
