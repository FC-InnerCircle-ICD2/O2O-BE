package org.fastcampus.cart.entity

data class CartMenu(
    val sequence: Long,
    val menuId: String,
    val quantity: Long,
    val optionGroups: List<CartMenuOptionGroup>,
    val _id: String? = null,
)
