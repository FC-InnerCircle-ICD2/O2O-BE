package org.fastcampus.applicationclient.order.service.event

data class OrderCancellationEvent(
    val storeId: String,
    val orderId: String,
)
