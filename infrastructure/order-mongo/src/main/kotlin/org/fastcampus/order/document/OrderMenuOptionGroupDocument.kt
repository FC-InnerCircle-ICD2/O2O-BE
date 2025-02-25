package org.fastcampus.order.document

import org.fastcampus.order.entity.OrderMenuOptionGroup
import org.springframework.data.mongodb.core.mapping.Field

class OrderMenuOptionGroupDocument(
    @Field(name = "id")
    val id: Long? = null,
    val orderMenuId: Long,
    val orderMenuOptionGroupName: String,
    val orderMenuOptionDocument: List<OrderMenuOptionDocument>? = null,
)

fun OrderMenuOptionGroupDocument.toModel() =
    OrderMenuOptionGroup(
        id,
        orderMenuId,
        orderMenuOptionGroupName,
        orderMenuOptionDocument?.map { it.toModel() },
    )
