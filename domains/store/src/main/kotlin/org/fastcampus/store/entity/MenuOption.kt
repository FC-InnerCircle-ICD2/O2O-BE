package org.fastcampus.store.entity

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class MenuOption(
    val id: String? = null,
    val name: String?,
    val price: Long?,
    val isSoldOut: Boolean,
    val order: Long?,
)
