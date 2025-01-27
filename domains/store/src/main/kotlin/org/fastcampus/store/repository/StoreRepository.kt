package org.fastcampus.store.repository

import org.fastcampus.store.entity.Store
import org.fastcampus.store.entity.StoreWithDistance

/**
 * Created by brinst07 on 25. 1. 11..
 */

interface StoreRepository {
    fun findByCategory(category: String): List<Store>
    fun findById(storeId: String): Store?
    fun findStoreNearbyAndCondition(latitude: Double, longitude: Double, category: Store.Category?, searchName: String?): List<StoreWithDistance>?
    fun findOwnerIdByStoreId(storeId: String): String?
}
