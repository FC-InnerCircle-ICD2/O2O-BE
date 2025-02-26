package org.fastcampus.applicationclient.order.service.event

import org.fastcampus.order.entity.Order

class OrderDetailSaveEvent(
    val order: Order,
    val paymentType: Map<String, String>,
)
