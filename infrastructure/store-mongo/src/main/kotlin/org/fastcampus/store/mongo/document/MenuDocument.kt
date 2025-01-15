package org.fastcampus.store.mongo.document

import org.fastcampus.store.entity.Menu

/**
 * Created by brinst07 on 25. 1. 11..
 */
class MenuDocument(
    val id: String? = null,
    val name: String?,
    val price: Long?,
    val desc: String?,
    val imgUrl: String?,
    val isSoldOut: Boolean,
    val isHided: Boolean,
    val storeId: String?,
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
        isSoldOut,
        isHided,
        storeId,
        menuOptionGroupDocument?.map { it.toModel() },
        order,
    )
