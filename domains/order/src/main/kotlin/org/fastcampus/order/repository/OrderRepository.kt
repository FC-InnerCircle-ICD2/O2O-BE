package org.fastcampus.order.repository

import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.common.dto.OffSetBasedDTO
import org.fastcampus.order.dto.OrderCount
import org.fastcampus.order.entity.Order
import java.time.LocalDateTime

/**
 * Created by brinst07 on 25. 1. 11..
 */
interface OrderRepository {
    fun save(order: Order): Order

    fun findById(id: String): Order?

    fun findAll(): List<Order>

    fun delete(id: Long): Boolean

    fun update(id: Long): Order

    fun findByUserIdExcludingWaitStatus(userId: Long, keyword: String, page: Int, size: Int): CursorDTO<Order>

    fun findByStoreIdAndStatusesWithPeriod(
        storeId: String,
        status: List<Order.Status>,
        startDateTime: LocalDateTime?,
        endDateTime: LocalDateTime?,
        page: Int,
        size: Int,
    ): OffSetBasedDTO<Order>

    fun findReviewableOrders(userId: Long): List<Order>

    fun findAllByStoreIdAndOrderTimeBetweenAndStatusIn(
        storeId: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        status: List<Order.Status>,
    ): List<Order>

    fun findOrderCnt(): List<OrderCount>
}
