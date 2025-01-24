package org.fastcampus.store.redis.repository

import org.fastcampus.store.redis.Coordinates
import org.fastcampus.store.redis.StoreRedisRepository
import org.springframework.data.geo.Point
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class StoreRedisRepositoryImpl(
    val redisTemplate: RedisTemplate<String, Any>,
) : StoreRedisRepository {
    companion object {
        const val GEO_KEY = "geo:store:location"
        const val DISTANCE_KEY = "distance:user:store"
        const val WINDOWDURATION = 60 * 60 * 12
    }

    override fun saveStoreLocation(storeId: String, coordinates: Coordinates) {
        redisTemplate.opsForGeo().add(
            GEO_KEY,
            Point(coordinates.longitude, coordinates.latitude),
            storeId,
        )
    }

    override fun getStoreLocation(storeId: String): Coordinates? {
        val position = redisTemplate.opsForGeo().position(GEO_KEY, storeId)?.firstOrNull()
        return position?.let { Coordinates(it.y, it.x) }
    }

    override fun getDistanceBetweenUserByStore(userKey: String, storeKey: String): Double {
        val key = "$DISTANCE_KEY:$userKey:$storeKey"
        val distance = redisTemplate.opsForValue().get(key) as? Double
        return distance ?: 0.0
    }

    override fun saveDistanceBetweenUserByStore(userKey: String, storeKey: String, distance: Double) {
        val key = "$DISTANCE_KEY:$userKey:$storeKey"
        redisTemplate.opsForValue().set(key, distance)
    }

    override fun saveUserLocation(userKey: String, coordinates: Coordinates) {
        redisTemplate.opsForGeo().add(
            GEO_KEY,
            Point(coordinates.longitude, coordinates.latitude),
            userKey,
        )
    }

    override fun getSuggestions(affix: String, page: Int, size: Int): List<String>? {
        val keys = redisTemplate.keys("suggest:$affix*")
        keys.addAll(redisTemplate.keys("suggest:?*$affix*"))
        return keys.map { it.removePrefix("suggest:") }
    }

    override fun existsByName(name: String): Boolean {
        val storeList = redisTemplate.keys("suggest:$name")
        return storeList.isNotEmpty()
    }

    override fun addSearch(keyword: String) {
        val currentTime = System.currentTimeMillis() / 1000
        redisTemplate.opsForZSet().add("search:$keyword", currentTime.toString(), currentTime.toDouble())
    }

    override fun getTrendKeywords(): Map<String, Long>? {
        val keywords = redisTemplate.keys("search:*")
        return keywords.associateWith { getRecentSearchCount(it.removePrefix("search:")) }
            .filterValues { it > 0 }
            .entries.sortedByDescending { it.value }
            .take(10)
            .associate { it.key.removePrefix("search:") to it.value }
    }

    override fun removeOldData() {
        val currentTime = System.currentTimeMillis() / 1000
        val thresholdTime = currentTime - WINDOWDURATION
        redisTemplate.keys("search:*").forEach { key ->
            redisTemplate.opsForZSet().removeRangeByScore(key, Double.NEGATIVE_INFINITY, thresholdTime.toDouble())
        }
    }

    private fun getRecentSearchCount(keyword: String): Long {
        val currentTime = System.currentTimeMillis() / 1000
        val startTime = currentTime - WINDOWDURATION
        return redisTemplate.opsForZSet().count("search:$keyword", startTime.toDouble(), currentTime.toDouble()) ?: 0
    }
}
