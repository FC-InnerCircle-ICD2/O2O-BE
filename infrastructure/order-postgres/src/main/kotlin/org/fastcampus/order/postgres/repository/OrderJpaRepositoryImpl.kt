package org.fastcampus.order.postgres.repository

import org.fastcampus.order.entity.Order
import org.fastcampus.order.postgres.entity.toJpaEntity
import org.fastcampus.order.postgres.entity.toModel
import org.fastcampus.order.repository.OrderRepository
import org.springframework.stereotype.Repository

@Repository
class OrderJpaRepositoryImpl(
    private val orderJpaRepository: OrderJpaRepository,
): OrderRepository {
    override fun save(order: Order): Order {
        return orderJpaRepository.save(order.toJpaEntity()).toModel()
    }

    override fun findById(id: Long): Order? {
        return orderJpaRepository.findById(id)
            .map { it.toModel() }
            .orElse(null)
    }

    override fun findAll(): List<Order> {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun update(id: Long): Order {
        TODO("Not yet implemented")
    }


}
