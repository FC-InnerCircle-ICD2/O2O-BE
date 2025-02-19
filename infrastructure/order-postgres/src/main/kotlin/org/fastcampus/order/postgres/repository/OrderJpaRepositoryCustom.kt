package org.fastcampus.order.postgres.repository

import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.common.dto.OffSetBasedDTO
import org.fastcampus.order.entity.Order
import org.fastcampus.order.postgres.entity.OrderJpaEntity
import org.fastcampus.order.postgres.entity.toJpaEntity
import org.fastcampus.order.postgres.entity.toModel
import org.fastcampus.order.repository.OrderRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

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

    override fun findByUserIdExcludingWaitStatus(userId: Long, keyword: String, page: Int, size: Int): CursorDTO<Order> {
        val pageable: Pageable = PageRequest.of(page, size, Sort.by("orderTime").descending())
        val orderJpaEntities: Page<OrderJpaEntity> = orderJpaRepository.findByUserIdAndStatusNot(
            userId,
            keyword,
            Order.Status.WAIT,
            pageable,
        )
        return CursorDTO(
            content = orderJpaEntities.content.map { it.toModel() },
            nextCursor = if (orderJpaEntities.nextPageable().sort.isSorted) orderJpaEntities.nextPageable().pageNumber else null,
        )
    }

    override fun findByStoreIdAndStatusesWithPeriod(
        storeId: String,
        status: List<Order.Status>,
        startDateTime: LocalDateTime?,
        endDateTime: LocalDateTime?,
        page: Int,
        size: Int,
    ): OffSetBasedDTO<Order> {
        val pageable: Pageable = PageRequest.of(page, size, Sort.by("orderTime").descending())

        var spec: Specification<OrderJpaEntity> = Specification.where(OrderSpecifications.hasStoreId(storeId))
            .and(OrderSpecifications.hasStatus(status))

        OrderSpecifications.orderTimeBetween(startDateTime, endDateTime)?.let {
            spec = spec.and(it)
        }

        val orderJpaEntities = orderJpaRepository.findAll(spec, pageable).map { it.toModel() }

        return OffSetBasedDTO(
            content = orderJpaEntities.content,
            currentPage = orderJpaEntities.number,
            totalPages = orderJpaEntities.totalPages,
            totalItems = orderJpaEntities.totalElements,
            hasNext = orderJpaEntities.hasNext(),
        )
    }

    override fun findReviewableOrders(userId: Long, cursor: LocalDateTime): List<Order> {
        // 리뷰는 주문 후 3일까지 가능
        val threeDaysAgo = LocalDate.now().minusDays(3).atStartOfDay()
        return orderJpaRepository.findByUserIdAndOrderTimeAfterWithCursor(userId, threeDaysAgo, cursor)
            .map { it.toModel() }
            .toList()
    }

    override fun findAllByStoreIdAndOrderTimeBetweenAndStatusIn(
        storeId: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        status: List<Order.Status>,
    ): List<Order> {
        return orderJpaRepository.findAllByStoreIdAndOrderTimeBetweenAndStatusIn(
            storeId,
            startDateTime,
            endDateTime,
            status,
        ).map { it.toModel() }
    }
}
