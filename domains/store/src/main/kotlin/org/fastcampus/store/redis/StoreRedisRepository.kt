package org.fastcampus.store.redis

data class Coordinates(val latitude: Double, val longitude: Double)

interface StoreRedisRepository {
    fun saveStoreLocation(storeId: String, coordinates: Coordinates)

    fun getStoreLocation(storeId: String): Coordinates?

    fun getDistanceBetweenUserByStore(userKey: String, storeKey: String): Double

    fun saveDistanceBetweenUserByStore(userKey: String, storeKey: String, distance: Double)

    fun saveUserLocation(userKey: String, coordinates: Coordinates)

    fun getSuggestions(affix: String, page: Int, size: Int): List<String>?

    fun addSearch(keyword: String)

    fun getTrendKeywords(): Map<String, Long>?

    fun removeOldData()

    fun existsByName(name: String): Boolean?
}
