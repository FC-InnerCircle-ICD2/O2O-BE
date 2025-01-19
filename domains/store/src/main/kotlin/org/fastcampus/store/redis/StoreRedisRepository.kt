package org.fastcampus.store.redis

import org.fastcampus.store.entity.TrendKeyword

data class Coordinates(val latitude: Double, val longitude: Double)

interface StoreRedisRepository {
    fun saveStoreLocation(storeId: String, coordinates: Coordinates)

    fun getStoreLocation(storeId: String): Coordinates?

    fun getDistanceBetweenUserByStore(userKey: String, storeKey: String): Double

    fun saveDistanceBetweenUserByStore(userKey: String, storeKey: String, distance: Double)

    fun saveUserLocation(userKey: String, coordinates: Coordinates)

    fun incrementSearchCount(keyword: String)

    fun getTrendKeywords(): List<TrendKeyword>?
}
