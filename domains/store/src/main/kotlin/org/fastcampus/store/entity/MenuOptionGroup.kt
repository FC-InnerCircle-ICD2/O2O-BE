package org.fastcampus.store.entity

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class MenuOptionGroup(
    val id: Long? = null,
    val name: String?,
    val menuId: String?,
    val minSel: String?,
    val maxSel: String?,
    val order: String?,
)
