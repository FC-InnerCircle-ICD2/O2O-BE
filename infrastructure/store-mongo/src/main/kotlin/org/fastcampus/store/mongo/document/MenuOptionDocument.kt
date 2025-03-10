package org.fastcampus.store.mongo.document

import org.fastcampus.store.entity.MenuOption
import org.springframework.data.mongodb.core.mapping.Field

/**
 * Created by brinst07 on 25. 1. 11..
 */

class MenuOptionDocument(
    @Field(name = "id")
    val id: String? = null,
    val name: String?,
    val price: Long?,
    val isSoldOut: String,
    val order: Long?,
)

fun MenuOptionDocument.toModel() =
    MenuOption(
        id,
        name,
        price,
        isSoldOut = isSoldOut == "Y",
        order,
    )
