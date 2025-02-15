package org.fastcampus.cart.entity

data class CartMenuOptionGroup(
    val groupId: String,
    val optionIds: List<String>,
    val _id: String? = null,
)
