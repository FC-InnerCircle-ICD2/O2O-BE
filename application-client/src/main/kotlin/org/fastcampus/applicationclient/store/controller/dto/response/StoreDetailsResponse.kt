package org.fastcampus.applicationclient.store.controller.dto.response

data class StoreInfo(
    val id: String?,
    val name: String?,
    val imageMain: String?,
    val rating: Double,
    val reviewCount: Int,
    val deliveryTime: String,
    val deliveryDistance: Double,
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

// 카테고리 정보 (변경됨)
data class CategoryResponse(
    val categoryId: String,
    val categoryName: String,
    val menus: List<MenuResponse>,
)

// 메뉴 정보 (변경됨)
data class MenuResponse(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Int, // String -> Int 변환 필요
    val soldOut: Boolean, // 필드명 변경 (isSoldOut -> soldout)
)
