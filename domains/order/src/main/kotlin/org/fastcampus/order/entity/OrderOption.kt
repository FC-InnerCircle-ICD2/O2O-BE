package org.fastcampus.order.entity

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class OrderOption(
    val id: Long? = null,
    val orderOptionGroupId: Long?,
    val menuOptionName: String?,
    val menuOptionPrice: Long?
)
