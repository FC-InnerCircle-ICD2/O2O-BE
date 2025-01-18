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
}
