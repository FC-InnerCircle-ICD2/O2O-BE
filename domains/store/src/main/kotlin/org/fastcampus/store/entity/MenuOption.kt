package org.fastcampus.store.entity

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class MenuOption(
    val id: Long? = null,
    val name: String?,
    val price: String?,
    val menuOptionGroupId: String?,
    val isSoldOut: String?,
    val order: String?,
)
