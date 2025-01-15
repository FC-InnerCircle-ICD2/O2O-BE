package org.fastcampus.order.entity

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class OrderOption(
    val id: String,
    val productOptionId: String?,
    val productOptionName: String?,
    val productOptionPrice: String?,
)
