package org.fastcampus.store.mongo.document

import org.fastcampus.store.entity.Menu
import org.springframework.data.mongodb.core.mapping.Field

/**
 * Created by brinst07 on 25. 1. 11..
 */
class MenuDocument(
    @Field(name = "id")
    val id: String? = null,
    val name: String?,
    val price: String?,
    val desc: String?,
    val imgUrl: String?,
    val isSoldOut: String,
    val isHided: String,
    val menuCategoryId: String?,
    @Field(name = "menuOptionGroups")
    val menuOptionGroupDocument: List<MenuOptionGroupDocument>?,
    val order: Long,
)

fun MenuDocument.toModel() =
    Menu(
        id,
        name,
        price,
        desc,
        imgUrl,
        isSoldOut = isSoldOut == "Y", // Y -> true, N -> false
        isHided = isHided == "Y", // Y -> true, N -> false
        menuCategoryId,
        menuOptionGroupDocument?.map { it.toModel() },
        order,
    )
