package org.fastcampus.store.mongo.document

import org.fastcampus.store.entity.MenuOptionGroup

/**
 * Created by brinst07 on 25. 1. 11..
 */

class MenuOptionGroupDocument(
    val id: String? = null,
    val name: String?,
    val menuId: String?,
    val minSel: Int?,
    val maxSel: Int?,
    val menuOptionDocument: List<MenuOptionDocument>?,
    val order: Long?,
)

fun MenuOptionGroupDocument.toModel() =
    MenuOptionGroup(
        id,
        name,
        menuId,
        minSel,
        maxSel,
        menuOptionDocument?.map { it.toModel() },
        order,
    )
