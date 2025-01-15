package org.fastcampus.order.repository

import org.fastcampus.order.entity.Order
import java.util.Optional

/**
 * Created by brinst07 on 25. 1. 11..
 */
interface OrderRepository {
    fun save(order: Order): Order
    fun findById(id: Long): Order?
    fun findAll(): List<Order>
    fun delete(id: Long): Boolean
    fun update(id: Long): Order
}
