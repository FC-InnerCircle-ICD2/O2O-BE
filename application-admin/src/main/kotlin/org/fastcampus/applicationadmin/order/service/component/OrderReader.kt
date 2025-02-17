package org.fastcampus.applicationadmin.order.service.component

import org.fastcampus.order.entity.Order
import org.fastcampus.order.repository.OrderMenuOptionGroupRepository
import org.fastcampus.order.repository.OrderMenuOptionRepository
import org.fastcampus.order.repository.OrderMenuRepository
import org.fastcampus.order.repository.OrderRepository
import org.springframework.stereotype.Component

@Component
class OrderReader(
    private val orderRepository: OrderRepository,
    private val orderMenuRepository: OrderMenuRepository,
    private val orderMenuOptionGroupRepository: OrderMenuOptionGroupRepository,
    private val orderMenuOptionRepository: OrderMenuOptionRepository,
) {
    fun findOrderByIdWithSubItems(orderId: String): Order {
        val order = orderRepository.findById(orderId)
        val orderMenus = orderMenuRepository.findByOrderId(orderId).map { orderMenu ->
            val orderMenuOptionGroups = orderMenuOptionGroupRepository.findByOrderMenuId(orderMenu.id!!)
            orderMenu.copy(
                orderMenuOptionGroups = orderMenuOptionGroups.map { optionGroup ->
                    val orderMenuOptions = orderMenuOptionRepository.findByOrderMenuOptionGroupId(optionGroup.id!!)
                    optionGroup.copy(orderMenuOptions = orderMenuOptions)
                },
            )
        }
        return order!!.copy(orderMenus = orderMenus)
    }
}
