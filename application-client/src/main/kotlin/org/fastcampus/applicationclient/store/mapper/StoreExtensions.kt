package org.fastcampus.applicationclient.store.mapper

import org.fastcampus.store.exception.StoreException
import org.fastcampus.store.redis.Coordinates
import org.fastcampus.store.redis.StoreRedisRepository
import org.fastcampus.store.repository.StoreRepository

fun Double.calculateDeliveryTime(): Int =
    when {
        this < 2000 -> 25
        this < 4000 -> 30
        this < 6000 -> 35
        this < 8000 -> 40
        this < 9000 -> 45
        else -> 60
    }

fun StoreRepository.fetchStoreCoordinates(storeId: String, redisRepository: StoreRedisRepository): Coordinates {
    return redisRepository.getStoreLocation(storeId)
        ?: this.findById(storeId)?.let { store ->
            Coordinates(
                latitude = store.latitude ?: 0.0,
                longitude = store.longitude ?: 0.0,
            ).also { redisRepository.saveStoreLocation(storeId, it) }
        }
        ?: throw StoreException.StoreCoordinatesNotFoundException(storeId)
}

fun StoreRedisRepository.fetchDistance(userCoordinates: Coordinates, storeId: String): Double {
    val userKey = "user:${userCoordinates.latitude},${userCoordinates.longitude}"
    return this.getDistanceBetweenUserByStore(userKey, storeId)
}
