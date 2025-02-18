package org.fastcampus.store.exception

sealed class StoreException(message: String) : RuntimeException(message) {
    data class StoreNotFoundException(val storeId: String) :
        StoreException("Store not found: $storeId")

    data class StoreCoordinatesNotFoundException(val storeId: String) :
        StoreException("Store coordinates not found for storeId=$storeId")

    data class DeliveryCalculationException(val storeId: String, override val cause: Throwable?) :
        StoreException("Failed to calculate delivery time for store $storeId: $cause")
}
