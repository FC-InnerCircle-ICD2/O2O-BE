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

    fun Store.toStoreInfo(deliveryTime: String, deliveryDistance: Double, rating: Double, reviewCount: Int): StoreInfo =
        StoreInfo(
            id = id ?: "unknown",
            name = name ?: "unknown",
            imageMain = imageMain ?: "unknown",
            rating = rating,
            reviewCount = reviewCount,
            deliveryTime = deliveryTime,
            deliveryDistance = deliveryDistance,
            freeDelivery = true,
            address = address ?: "unknown",
            latitude = latitude ?: 0.0,
            longitude = longitude ?: 0.0,
            phone = tel ?: "unknown",
            minimumOrderAmount = minimumOrderAmount,
        )

    fun Store.toStoreInfo(distance: Double, rating: Double, reviewCount: Int): StoreInfo =
        StoreInfo(
            id = id ?: "unknown",
            name = name ?: "unknown",
            imageMain = imageMain ?: "unknown",
            rating = rating,
            reviewCount = reviewCount,
            deliveryTime = distance.calculateDeliveryTime().toString(),
            deliveryDistance = distance,
            freeDelivery = true,
            address = address ?: "unknown",
            latitude = latitude ?: 0.0,
            longitude = longitude ?: 0.0,
            phone = tel ?: "unknown",
            minimumOrderAmount = minimumOrderAmount,
        )

    fun Store.toStoreInfo(distance: Double): StoreInfo =
        StoreInfo(
            id = id ?: "unknown",
            name = name ?: "unknown",
            imageMain = imageMain ?: "unknown",
            rating = roundedRating(rating), // null이면 0.0 반환
            reviewCount = reviewCount ?: 0,
            deliveryTime = distance.calculateDeliveryTime().toString(),
            deliveryDistance = distance,
            freeDelivery = true,
            address = address ?: "unknown",
            latitude = latitude ?: 0.0,
            longitude = longitude ?: 0.0,
            phone = tel ?: "unknown",
            minimumOrderAmount = minimumOrderAmount,
        )

    private fun roundedRating(rating: Float?): Double {
        val rawRating = rating?.toDouble() ?: 0.0
        // 소수점 한 자리까지 반올림
        val roundedRating = Math.round(rawRating * 10) / 10.0
        return roundedRating
    }

    private fun Menu.toMenuResponse(): MenuOptionInfo {
        return MenuOptionInfo(
            id = this.id ?: "unknown",
            name = this.name ?: "unknown",
            price = this.price
                ?.takeIf { it.isNotBlank() }
                ?.replace(",", "")
                ?.toIntOrNull()
                ?: -1,
            description = this.desc ?: "",
            imageUrl = this.imgUrl ?: "unknown",
            soldOut = this.isSoldOut,
            isBest = isBest,
            isManyOrder = isManyOrder,
        )
    }
}
