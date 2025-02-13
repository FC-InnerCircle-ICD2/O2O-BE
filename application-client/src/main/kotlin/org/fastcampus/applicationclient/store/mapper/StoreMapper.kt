package org.fastcampus.applicationclient.store.mapper

import org.fastcampus.applicationclient.store.controller.dto.response.CategoryResponse
import org.fastcampus.applicationclient.store.controller.dto.response.MenuOptionInfo
import org.fastcampus.applicationclient.store.controller.dto.response.StoreInfo
import org.fastcampus.store.entity.Menu
import org.fastcampus.store.entity.Store
import org.fastcampus.store.entity.StoreMenuCategory

object StoreMapper {
    fun StoreMenuCategory.toCategoryInfo(): CategoryResponse {
        return CategoryResponse(
            categoryId = this.id ?: "unknown",
            categoryName = this.name ?: "unknown",
            menus = this.menu?.map { it.toMenuResponse() } ?: emptyList(),
        )
    }

    fun Store.toStoreInfo(deliveryTime: String, deliveryDistance: Double): StoreInfo =
        StoreInfo(
            id = id ?: "unknown",
            name = name ?: "unknown",
            imageMain = imageMain ?: "unknown",
            rating = 3.8,
            reviewCount = 3000,
            deliveryTime = deliveryTime,
            deliveryDistance = deliveryDistance,
            freeDelivery = true,
            address = address ?: "unknown",
            latitude = latitude ?: 0.0,
            longitude = longitude ?: 0.0,
            phone = tel ?: "unknown",
        )

    fun Store.toStoreInfo(distance: Double): StoreInfo =
        StoreInfo(
            id = id ?: "unknown",
            name = name ?: "unknown",
            imageMain = imageMain ?: "unknown",
            rating = 3.8,
            reviewCount = 3000,
            deliveryTime = distance.calculateDeliveryTime().toString(),
            deliveryDistance = distance,
            freeDelivery = true,
            address = address ?: "unknown",
            latitude = latitude ?: 0.0,
            longitude = longitude ?: 0.0,
            phone = tel ?: "unknown",
        )

    private fun Menu.toMenuResponse(): MenuOptionInfo {
        return MenuOptionInfo(
            id = this.id ?: "unknown",
            name = this.name ?: "unknown",
            price = this.price?.replace(",", "")?.toInt() ?: -1,
            description = this.desc ?: "",
            imageUrl = this.imgUrl ?: "unknown",
            soldOut = this.isSoldOut,
        )
    }
}
