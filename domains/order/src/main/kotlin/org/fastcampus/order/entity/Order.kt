package org.fastcampus.order.entity

import java.time.LocalDateTime

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class Order(
    val id: String,
    val storeId: Long?,
    val userId: Long?,
    val roadAddress: String?,
    val jibunAddress: String?,
    val detailAddress: String?,
    val tel: String?,
    val orderStatus: OrderStatus?,
    val orderTime: LocalDateTime?,
    val orderType: OrderType?,
    val paymentType: PaymentType?,
    val isDeleted: Boolean,
    val deliveryTime: LocalDateTime?,
    val deliveryPrice: Long?
)
