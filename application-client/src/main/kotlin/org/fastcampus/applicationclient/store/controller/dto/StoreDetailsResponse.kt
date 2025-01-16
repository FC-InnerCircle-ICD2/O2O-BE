package org.fastcampus.applicationclient.store.controller.dto

data class StoreDetailsResponse (
    val store: StoreInfo,
    val categories: List<CategoryInfo>
)

data class StoreInfo (
    val id: String,
    val name: String,
    val imageMain: String?,
    val rating: Double,
    val reviewCount: Int,
    val deliveryTime: String,
    val freeDelivery: Boolean,
)

data class CategoryInfo (
    val categoryName: String,
    val menus: List<MenuInfo>,
)

data class MenuInfo(
    val id: String,
    val name: String,
    val price: String,
    val description: String?,
    val imageUrl: String?,
    val isSoldOut: Boolean,
)
