package org.fastcampus.store.mongo.utils

import org.springframework.data.geo.Distance

/**
 * Created by brinst07 on 25. 1. 21.
 */
object DistanceUtils {
//    fun formatDistance(distance: Double?): String {
//        return if (distance != null) {
//            if (distance < 0.0001) {
//                // Convert to nanometers or something meaningful based on the scale
//                "${String.format("%.12f", distance * 1_000_000_000)} nm" // nanometers
//            } else if (distance < 1000) {
//                "${String.format("%.2f", distance)} m" // meters
//            } else {
//                "${String.format("%.2f", distance / 1000)} km" // kilometers
//            }
//        } else {
//            "N/A"
//        }
//    }

    // 거리 변환 함수 (radian -> meter)
    fun convertDistanceToMeters(distance: Distance): Double {
        // Distance의 value는 기본적으로 km로 반환됨
        return distance.value * 1000 // km를 meter로 변환
    }

    // 거리 포맷팅 함수
    fun formatDistance(distance: Distance): String {
        val distanceInMeters = convertDistanceToMeters(distance) // 거리 값을 미터 단위로 변환
        return if (distanceInMeters >= 1000) {
            String.format("%.2f km", distanceInMeters / 1000) // 1,000m 이상일 경우 km로 표시
        } else {
            String.format("%d m", distanceInMeters.toInt()) // 1,000m 미만일 경우 m로 표시
        }
    }
}
