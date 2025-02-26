package org.fastcampus.order.repository

import org.fastcampus.order.entity.OrderDetail

interface OrderDetailRepository {
    fun saveOrderDetail(orderDetail: OrderDetail): OrderDetail

    fun findById(orderId: String): OrderDetail?
}
