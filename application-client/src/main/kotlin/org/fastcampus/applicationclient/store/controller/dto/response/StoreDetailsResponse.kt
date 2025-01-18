package org.fastcampus.applicationclient.store.controller.dto.response

data class StoreDetailsResponse(
    val store: StoreInfo,
    val categories: List<CategoryInfo>,
)

data class StoreInfo(
    val id: String,
    val name: String,
    val imageMain: String?,
    val rating: Double,
    val reviewCount: Int,
    val deliveryTime: String,
    val freeDelivery: Boolean,
)

data class CategoryInfo(
    val categoryId: Int,
    val categoryName: String,
    val menus: List<MenuInfo>,
    val page: Int,
    val size: Int,
    val totalPages: Int,
    val totalElements: Long,
)

data class MenuInfo(
    val id: String,
    val name: String,
    val price: String,
    val description: String?,
    val imageUrl: String?,
    val isSoldOut: Boolean,
)
