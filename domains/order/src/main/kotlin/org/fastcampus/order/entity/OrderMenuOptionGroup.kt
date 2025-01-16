package org.fastcampus.order.entity

data class OrderMenuOptionGroup(
    val id: Long? = null,
    val orderMenuId: Long,
    val orderMenuOptionGroupName: String
)
