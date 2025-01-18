package org.fastcampus.order.repository

import org.fastcampus.order.entity.OrderMenuOptionGroup

interface OrderMenuOptionGroupRepository {
    fun findByOrderMenuId(orderMenuId: Long): List<OrderMenuOptionGroup>
}
