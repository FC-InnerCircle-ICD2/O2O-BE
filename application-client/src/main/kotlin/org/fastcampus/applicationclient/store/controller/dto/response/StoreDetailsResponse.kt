package org.fastcampus.applicationclient.store.controller.dto.response

data class StoreInfo(
    val id: String?,
    val name: String?,
    val imageMain: String?,
    val rating: Double,
    val reviewCount: Int,
    val deliveryTime: String,
    val freeDelivery: Boolean,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val phone: String?,
)

data class CategoryInfo(
    val categoryId: String,
    val categoryName: String,
    val menus: List<MenuInfo>?,
)

data class MenuInfo(
    val id: String,
    val name: String,
    val price: String,
    val description: String,
    val imageUrl: String,
    val isSoldOut: Boolean,
    val menuOptionGroupIds: List<String>,
)

data class MenuOptionGroupInfo(
    val id: String,
    val name: String,
    val options: List<MenuOptionInfo>,
)

data class MenuOptionInfo(
    val id: String,
    val name: String,
    val price: String,
    val isSoldOut: Boolean,
)
