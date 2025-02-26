package org.fastcampus.order.document

import org.fastcampus.order.entity.OrderMenuOption
import org.springframework.data.mongodb.core.mapping.Field

class OrderMenuOptionDocument(
    @Field(name = "id")
    val id: Long? = null,
    val orderMenuOptionGroupId: Long,
    val menuOptionName: String,
    val menuOptionPrice: Long,
)

fun OrderMenuOption.toJpaDocument() =
    OrderMenuOptionDocument(
        id,
        orderMenuOptionGroupId,
        menuOptionName,
        menuOptionPrice,
    )

fun OrderMenuOptionDocument.toModel() =
    OrderMenuOption(
        id,
        orderMenuOptionGroupId,
        menuOptionName,
        menuOptionPrice,
    )
