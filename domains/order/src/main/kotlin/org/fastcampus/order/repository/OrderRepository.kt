package org.fastcampus.order.repository

import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.order.entity.Order

/**
 * Created by brinst07 on 25. 1. 11..
 */
interface OrderRepository {
    fun save(order: Order): Order

    fun findById(id: String): Order?

    fun findAll(): List<Order>

    fun delete(id: Long): Boolean

    fun update(id: Long): Order

    fun findByUserId(userId: Long, page: Int, size: Int): CursorDTO<Order>
}
