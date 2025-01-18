package org.fastcampus.applicationclient.order.controller.dto.request

import org.fastcampus.order.entity.Order
import org.fastcampus.payment.entity.Payment

data class OrderCreationRequest(
    val storeId: String,
    val roadAddress: String,
    val jibunAddress: String,
    val detailAddress: String,
    val orderType: Order.Type,
    val paymentType: Payment.Type,
    val orderMenus: List<OrderMenu>,
)

data class OrderMenu(
    val id: String,
    val quantity: Int,
    val orderMenuOptionGroups: List<OrderMenuOptionGroup>,
)

data class OrderMenuOptionGroup(
    val id: String,
    val orderMenuOptionIds: List<String>,
)
