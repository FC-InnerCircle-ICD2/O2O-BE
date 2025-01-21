package org.fastcampus.applicationclient.order.controller.dto.response

data class OrderMenuOptionGroupResponse(
    val id: Long?,
    val orderMenuId: Long,
    val orderMenuOptionGroupName: String,
    val orderMenuOptions: List<OrderMenuOptionResponse>,
)
