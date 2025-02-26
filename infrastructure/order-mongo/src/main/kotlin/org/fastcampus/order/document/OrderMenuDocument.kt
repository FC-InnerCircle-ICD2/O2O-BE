package org.fastcampus.order.document

import org.fastcampus.order.entity.OrderMenu
import org.springframework.data.mongodb.core.mapping.Field

class OrderMenuDocument(
    @Field(name = "id")
    val id: Long? = null,
    val menuId: String,
    val menuName: String,
    val menuQuantity: Long,
    val menuPrice: Long,
    val totalPrice: Long,
    val orderMenuOptionGroupDocument: List<OrderMenuOptionGroupDocument>? = null,
)

fun OrderMenuDocument.toModel() =
    OrderMenu(
        id,
        "",
        menuId,
        menuName,
        menuQuantity,
        menuPrice,
        totalPrice,
        orderMenuOptionGroupDocument?.map { it.toModel() },
    )
