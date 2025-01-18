package org.fastcampus.order.postgres.repository

import org.fastcampus.common.dto.CursorBasedDTO
import org.fastcampus.order.entity.Order
import org.fastcampus.order.postgres.entity.OrderJpaEntity
import org.fastcampus.order.postgres.entity.toJpaEntity
import org.fastcampus.order.postgres.entity.toModel
import org.fastcampus.order.repository.OrderRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository
class OrderJpaRepositoryCustom(
    private val orderJpaRepository: OrderJpaRepository,
) : OrderRepository {
    override fun save(order: Order): Order {
        return orderJpaRepository.save(order.toJpaEntity()).toModel()
    }

    override fun findById(id: String): Order? {
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

    override fun findByUserId(userId: Long, page: Int, size: Int): CursorBasedDTO<Order> {
        val pageable: Pageable = PageRequest.of(page, size, Sort.by("orderTime").descending())
        val orderJpaEntities: Page<OrderJpaEntity> = orderJpaRepository.findByUserId(userId, pageable)
        return CursorBasedDTO(
            isEnd = orderJpaEntities.isLast,
            totalCount = orderJpaEntities.totalElements,
            content = orderJpaEntities.content.map { it.toModel() },
        )
    }
}
