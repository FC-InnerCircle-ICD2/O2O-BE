package org.fastcampus.store.entity

data class StoreMenuCategory(
    val id: String? = null,
    val name: String?,
    val storeId: String?,
    val menu: List<Menu>?,
    val order: Long,
)
