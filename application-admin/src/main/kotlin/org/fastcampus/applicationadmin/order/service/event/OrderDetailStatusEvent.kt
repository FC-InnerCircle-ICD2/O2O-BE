package org.fastcampus.applicationadmin.order.service.event

import org.fastcampus.order.entity.Order

class OrderDetailStatusEvent(
    val orderId: String,
    val status: Order.Status,
)
