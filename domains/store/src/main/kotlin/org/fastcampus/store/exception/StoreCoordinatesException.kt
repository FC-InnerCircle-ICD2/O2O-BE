package org.fastcampus.store.exception

import org.fastcampus.store.entity.Store
import org.fastcampus.store.redis.Coordinates

sealed class StoreCoordinatesException(message: String) : RuntimeException(message) {
    data class InvalidLatitudeException(val storeId: String?) :
        StoreCoordinatesException("Store latitude is missing for store: $storeId")

    data class InvalidLongitudeException(val storeId: String?) :
        StoreCoordinatesException("Store longitude is missing for store: $storeId")
}

private fun Store.getCoordinates(): Coordinates? =
    try {
        when {
            latitude == null -> throw StoreCoordinatesException.InvalidLatitudeException(id)
            longitude == null -> throw StoreCoordinatesException.InvalidLongitudeException(id)
            else -> Coordinates(latitude, longitude)
        }
    } catch (e: StoreCoordinatesException) {
        null
    }
