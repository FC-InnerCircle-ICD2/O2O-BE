package org.fastcampus.store.utils

/**
 * Created by brinst07 on 25. 1. 20.
 */
object DeliveryUtils {
    fun calculateDeliveryTimeByDistance(
        distance: Double,
    ): Int {
        return when {
            distance < 5 -> 25
            distance < 10 -> 30
            distance < 20 -> 35
            distance < 30 -> 40
            distance < 40 -> 45
            else -> 60 // 40km 이상
        }
    }
}
