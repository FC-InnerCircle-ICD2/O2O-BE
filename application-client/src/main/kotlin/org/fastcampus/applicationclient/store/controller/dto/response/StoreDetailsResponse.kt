package org.fastcampus.applicationclient.store.controller.dto.response

import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.store.entity.Menu
import org.fastcampus.store.entity.Store

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

fun Store.toStoreInfo(deliveryTime: String): StoreInfo =
    StoreInfo(
        id = id ?: "unknown",
        name = name ?: "unknown",
        imageMain = imageMain ?: "unknown",
        rating = 3.8,
        reviewCount = 3000,
        deliveryTime = deliveryTime,
        freeDelivery = true,
        address = address ?: "unknown",
        latitude = latitude ?: 0.0,
        longitude = longitude ?: 0.0,
        phone = tel ?: "unknown",
    )

fun Menu.toMenuResponse(): MenuInfo {
    return MenuInfo(
        id = this.id ?: "unknown",
        name = this.name ?: "unknown",
        price = this.price ?: "가격 정보 없음",
        description = this.desc ?: "",
        imageUrl = this.imgUrl ?: "unknown",
        isSoldOut = this.isSoldOut,
        menuOptionGroupIds = this.menuOptionGroup?.map { it.id ?: "" } ?: emptyList(),
    )
}

fun <T> List<T>.paginate(cursor: Int, size: Int): CursorDTO<T> {
    val adjustedCursor = (cursor - 1).coerceAtLeast(0)
    val startIndex = adjustedCursor * size
    val endIndex = (startIndex + size).coerceAtMost(this.size)

    val content = this.subList(startIndex, endIndex)
    val nextCursor = if (endIndex < this.size) cursor + 1 else null

    return CursorDTO(content, nextCursor)
}
