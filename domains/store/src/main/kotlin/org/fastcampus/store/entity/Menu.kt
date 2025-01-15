package org.fastcampus.store.entity

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class Menu(
    val id: String? = null,
    val name: String?,
    val price: Long?,
    val desc: String?,
    val imgUrl: String?,
    val isSoldOut: Boolean,
    val isHided: Boolean,
    val storeId: String?,
    val menuOptionGroup: List<MenuOptionGroup>?,
    val order: Long,
)
