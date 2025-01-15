package org.fastcampus.store.mongo.document

import org.fastcampus.common.entity.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "TB_STORE_MENU_CATEGORY")
class StoreMenuCategoryDocument(
    @Id
    val id: Long? = null,
    val name: String?,
    val storeId: String?,
) : BaseEntity()
