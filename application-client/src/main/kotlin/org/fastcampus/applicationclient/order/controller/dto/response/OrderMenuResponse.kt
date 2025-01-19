package org.fastcampus.applicationclient.order.controller.dto.response

data class OrderMenuResponse(
    val id: Long?,
    val menuId: String,
    val menuName: String,
    val menuQuantity: Long,
    val menuPrice: Long,
    val totalPrice: Long,
    val orderMenuOptionGroups: List<OrderMenuOptionGroupResponse>,
)
