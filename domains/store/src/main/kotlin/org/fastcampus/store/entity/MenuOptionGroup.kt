package org.fastcampus.store.entity

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class MenuOptionGroup(
    val id: String? = null,
    val name: String?,
    val menuId: String?,
    val minSel: Int?,
    val maxSel: Int?,
    val menuOption: List<MenuOption>?,
    val order: Long?,
)
