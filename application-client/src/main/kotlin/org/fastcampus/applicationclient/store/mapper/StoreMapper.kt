package org.fastcampus.applicationclient.store.mapper

import org.fastcampus.applicationclient.store.controller.dto.response.CategoryInfo
import org.fastcampus.applicationclient.store.controller.dto.response.MenuInfo
import org.fastcampus.applicationclient.store.controller.dto.response.StoreInfo
import org.fastcampus.store.entity.Menu
import org.fastcampus.store.entity.Store
import org.fastcampus.store.entity.StoreMenuCategory

object StoreMapper {
    fun StoreMenuCategory.toCategoryInfo(): CategoryInfo {
        return CategoryInfo(
            categoryId = this.id ?: "unknown",
            categoryName = this.name ?: "unknown",
            menus = this.menu?.map { it.toMenuResponse() } ?: emptyList(),
        )
    }

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

    private fun Menu.toMenuResponse(): MenuInfo {
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
}
