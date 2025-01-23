package org.fastcampus.store.mongo.document

import org.fastcampus.store.entity.StoreMenuCategory
import org.springframework.data.mongodb.core.mapping.Field

class StoreMenuCategoryDocument(
    @Field(name = "id")
    val id: String? = null,
    val name: String?,
    val storeId: String?,
    @Field(name = "menus")
    val menuDocument: List<MenuDocument>?,
    val order: Long,
)

fun StoreMenuCategoryDocument.toModel(): StoreMenuCategory {
    return StoreMenuCategory(
        id,
        name,
        storeId,
        menuDocument?.map { it.toModel() },
        order,
    )
}
