package org.fastcampus.store.repository

import org.fastcampus.store.entity.Store
import org.fastcampus.store.entity.StoreWithDistance

/**
 * Created by brinst07 on 25. 1. 11..
 */

interface StoreRepository {
    fun findByCategory(category: String): List<Store>

    fun findById(storeId: String): Store?

    fun findStoreNearByAndCondition(storeId: String, latitude: Double, longitude: Double): StoreWithDistance

    fun findOwnerIdByStoreId(storeId: String): String?

    fun findByOwnerId(ownerId: String): String

    fun findStoreNearbyAndCondition(
        latitude: Double,
        longitude: Double,
        category: Store.Category?,
        searchName: String?,
        page: Int,
        size: Int,
    ): Pair<List<StoreWithDistance>, Int?>

    fun findStoreNearByAndConditionWithCursor(
        latitude: Double,
        longitude: Double,
        category: Store.Category?,
        searchName: String?,
        cursorDistance: Double?,
        cursorStoreId: String?,
        size: Int,
    ): Pair<List<StoreWithDistance>, String?>

    fun existsStoreNearBy(storeId: String, latitude: Double, longitude: Double, distanceKM: Double): Boolean
}
