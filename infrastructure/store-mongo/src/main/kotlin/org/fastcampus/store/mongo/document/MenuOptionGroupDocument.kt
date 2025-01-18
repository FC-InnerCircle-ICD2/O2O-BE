package org.fastcampus.store.mongo.document

import org.fastcampus.store.entity.MenuOptionGroup
import org.springframework.data.mongodb.core.mapping.Field

/**
 * Created by brinst07 on 25. 1. 11..
 */

class MenuOptionGroupDocument(
    val id: String? = null,
    val name: String?,
    val menuId: String?,
    val minSel: String?,
    val maxSel: String?,
    @Field(name = "menuOptions")
    val menuOptionDocument: List<MenuOptionDocument>?,
    val order: Long?,
)

fun MenuOptionGroupDocument.toModel() =
    MenuOptionGroup(
        id,
        name,
        menuId,
        minSel = minSel?.toIntWithoutUnit() ?: 0, // "1개" → 1로 변환
        maxSel = maxSel?.toIntWithoutUnit() ?: 0, // "2개" → 2로 변환
        menuOptionDocument?.map { it.toModel() },
        order,
    )

private fun String.toIntWithoutUnit(): Int? {
    return this.replace("개", "").toIntOrNull() // 기본 확장 함수 호출
}


