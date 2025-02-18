package org.fastcampus.order.repository

import org.fastcampus.order.entity.OrderMenu

interface OrderMenuRepository {
    fun findByOrderId(orderId: String): List<OrderMenu>

    fun save(orderMenu: OrderMenu): OrderMenu
}
