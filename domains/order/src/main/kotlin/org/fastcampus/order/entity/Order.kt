package org.fastcampus.order.entity


/**
 * Created by brinst07 on 25. 1. 11..
 */

data class Order(

    val id: Long? = null,

    val storeId: Long?,
    val roadAddress: String?,
    val jibunAddress: String?,
    val status: String?,
    val orderTime: String?,
    val type: String?,
    val isDeleted: String?,
    val deliveryTime: String?,
)

