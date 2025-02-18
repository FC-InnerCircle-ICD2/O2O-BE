package org.fastcampus.applicationclient.order.controller.dto.response

data class OrderMenuOptionResponse(
    val id: Long?,
    val orderMenuOptionGroupId: Long,
    val menuOptionName: String,
    val menuOptionPrice: Long,
)
