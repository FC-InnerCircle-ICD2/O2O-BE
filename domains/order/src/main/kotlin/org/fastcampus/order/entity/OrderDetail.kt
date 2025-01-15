package org.fastcampus.order.entity

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class OrderDetail(
    val id: Long? = null,
    val orderId: String?,
    val price: Long?,
    val productName: String?,
    val productQuantity: Long?,
    val productPrice: Long?,
)
