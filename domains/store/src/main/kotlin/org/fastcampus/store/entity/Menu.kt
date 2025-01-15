package org.fastcampus.store.entity

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class Menu(

    val id: Long? = null,
    val name: String?,
    val price: String?,
    val desc: String?,
    val imgUrl: String?,
    val isSoldOut: String?,
    val isHided: String?,
    val storeId: String?,
)
