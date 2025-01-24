package org.fastcampus.applicationclient.order.service.event

import org.fastcampus.order.entity.Order

data class OrderNotificationEvent(
    val order: Order,
)
