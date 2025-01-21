package org.fastcampus.store.mongo.document

import org.fastcampus.store.entity.StoreMenuCategory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.mapping.Field

private val logger: Logger = LoggerFactory.getLogger(StoreMenuCategoryDocument::class.java)
class StoreMenuCategoryDocument(
    val id: String? = null,
    val name: String?,
    val storeId: String?,
    @Field(name = "menus")
    val menuDocument: List<MenuDocument>?,
    val order: Long,
)

fun StoreMenuCategoryDocument.toModel(): StoreMenuCategory {
    logger.debug("Converting StoreMenuCategoryDocument to StoreMenuCategory: $id")
    return StoreMenuCategory(
        id,
        name,
        storeId,
        menuDocument?.map { it.toModel() },
        order,
    )
}

