package org.fastcampus.order.entity

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class OrderDetail(

    val id: String,
    val orderId: Long?,
    val price: String?,
    val productId: String?,
    val productName: String?,
    val productQuantity: String?,
    val productPrice: String?,
)
