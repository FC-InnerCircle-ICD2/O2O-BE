package org.fastcampus.store.mongo.document

import org.fastcampus.common.entity.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by brinst07 on 25. 1. 11..
 */
@Document(collection = "TB_MENU")
class MenuDocument(
    @Id
    val id: Long? = null,
    val name: String?,
    val price: String?,
    val desc: String?,
    val imgUrl: String?,
    val isSoldOut: String?,
    val isHided: String?,
    val storeId: String?,
) : BaseEntity()
