package org.fastcampus.favorite.entity

data class Favorite(
    val id: Long? = null,
    val userId: Long,
    val storeId: String,
)
