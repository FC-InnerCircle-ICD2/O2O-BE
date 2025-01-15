package org.fastcampus.store.mongo.document

import org.fastcampus.common.entity.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by brinst07 on 25. 1. 11..
 */

@Document(collection = "TB_MENU_OPTION_GROUP")
class MenuOptionGroupDocument(
    @Id
    val id: Long? = null,
    val name: String?,
    val menuId: String?,
    val minSel: String?,
    val maxSel: String?,
    val order: String?,
) : BaseEntity()
