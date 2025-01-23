package org.fastcampus.order.entity

data class OrderMenuOptionGroup(
    val id: Long? = null,
    val orderMenuId: Long,
    val orderMenuOptionGroupName: String,
    val orderMenuOptions: List<OrderMenuOption>? = null, // 필요시 설정
)
