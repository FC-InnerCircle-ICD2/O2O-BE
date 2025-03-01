package org.fastcampus.applicationclient.order.controller.dto.response

import org.fastcampus.order.entity.Order

data class OrderStatusResponse(
    val status: Order.ClientStatus,
)
