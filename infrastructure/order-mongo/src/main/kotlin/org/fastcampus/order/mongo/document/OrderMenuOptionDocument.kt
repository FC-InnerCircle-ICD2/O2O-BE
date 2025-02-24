package org.fastcampus.order.mongo.document

import org.fastcampus.order.entity.OrderMenuOption
import org.springframework.data.mongodb.core.mapping.Field

class OrderMenuOptionDocument(
    @Field(name = "id")
    val id: Long? = null,
    val orderMenuOptionGroupId: Long,
    val menuOptionName: String,
    val menuOptionPrice: Long,
)

fun OrderMenuOptionDocument.toModel() =
    OrderMenuOption(
        id,
        orderMenuOptionGroupId,
        menuOptionName,
        menuOptionPrice,
    )
