package org.fastcampus.order.repository

import org.fastcampus.order.entity.OrderMenuOption

interface OrderMenuOptionRepository {
    fun findByOrderMenuOptionGroupId(orderMenuOptionGroupId: Long): List<OrderMenuOption>

    fun save(orderMenuOption: OrderMenuOption): OrderMenuOption
}
