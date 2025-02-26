package org.fastcampus.applicationclient.order.service.event

import org.fastcampus.order.entity.OrderDetail

class OrderDetailSaveEvent(
    val orderDetail: OrderDetail,
)
