package org.fastcampus.cart.entity

data class Cart(
    val userId: Long,
    val storeId: String,
    val orderMenus: List<CartMenu>,
    val _id: String? = null,
)
