package org.fastcampus.store.mongo.document

import org.fastcampus.common.entity.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by brinst07 on 25. 1. 11..
 */

@Document(collection = "TB_MENU_OPTION")
class MenuOptionDocument(
    @Id
    val id: Long? = null,
    val name: String?,
    val price: String?,
    val menuOptionGroupId: String?,
    val isSoldOut: String?,
    val order: String?,
) : BaseEntity()
