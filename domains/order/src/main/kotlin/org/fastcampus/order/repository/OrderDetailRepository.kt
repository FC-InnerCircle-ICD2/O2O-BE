package org.fastcampus.order.repository

import org.fastcampus.order.entity.Order

interface OrderDetailRepository {
    fun saveOrder(order: Order, paymentType: Map<String, String>): Order

    fun findById(orderId: String): Order?
}
