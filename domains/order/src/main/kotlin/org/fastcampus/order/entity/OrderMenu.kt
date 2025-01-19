package org.fastcampus.order.entity

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class OrderMenu(
    val id: Long? = null,
    val orderId: String?,
    val menuId: String,
    val menuName: String,
    val menuQuantity: Long,
    val menuPrice: Long,
    val totalPrice: Long,
    val orderMenuOptionGroups: List<OrderMenuOptionGroup>? = null, // 필요시 설정
)
