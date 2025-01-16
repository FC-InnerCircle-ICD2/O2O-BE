package org.fastcampus.applicationclient.store.service

import org.fastcampus.applicationclient.store.controller.dto.CategoryInfo
import org.fastcampus.applicationclient.store.controller.dto.MenuInfo
import org.fastcampus.applicationclient.store.controller.dto.StoreDetailsResponse
import org.fastcampus.applicationclient.store.controller.dto.StoreInfo
import org.fastcampus.store.redis.Coordinates
import org.fastcampus.store.redis.StoreRedisRepository
import org.fastcampus.store.repository.StoreRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by brinst07 on 25. 1. 11..
 */
@Service
class StoreService(
    private val storeRepository: StoreRepository,
    private val storeRedisRepository: StoreRedisRepository,
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(StoreService::class.java)
    }

    @Transactional(readOnly = true)
    fun getStoreDetails(storeId: String, userCoordinates: Coordinates): StoreDetailsResponse {
        logger.info("Fetching store details for storeId: $storeId, userCoordinates: $userCoordinates")
        val store = storeRepository.findById(storeId)
            ?: throw IllegalArgumentException("Store not found: $storeId")
        logger.info("Store found: ${store.name}")
        // 거리 및 배달 시간 계산
        val deliveryInfo = calculateDeliveryTime(storeId, userCoordinates)

        // 응답 데이터 변환
        return StoreDetailsResponse(
            store = StoreInfo(
                id = store._id ?: "",
                name = store.name ?: "",
                imageMain = store.imageMain ?: "",
                rating = 4.5, // 예시: 평점 데이터 추가 로직 필요
                reviewCount = 150, // 예시: 리뷰 개수 데이터 추가 로직 필요
                deliveryTime = deliveryInfo["deliveryTime"] as String,
                freeDelivery = (store.border ?: 0) > 5000 // 예시: 무료 배달 여부
            ),
            categories = store.storeMenuCategory?.map { category ->
                logger.info("Mapping category: $category")
                CategoryInfo(
                    categoryName = category.name ?: "Unknown",
                    menus = category.menu?.map { menu ->
                        MenuInfo(
                            id = menu.id ?: "",
                            name = menu.name ?: "Unknown",
                            price = "${menu.price ?: 0}원",
                            description = menu.desc ?: "",
                            imageUrl = menu.imgUrl ?: "",
                            isSoldOut = menu.isSoldOut
                        )
                    } ?: emptyList()
                )
            } ?: emptyList()
        ).also {
            logger.info("StoreDetailsResponse created: ${it.store.name}")
        }
    }

    /**
     * 배달 예상 시간을 계산하는 메서드
     */
    fun calculateDeliveryTime(storeId: String, userCoordinates: Coordinates): Map<String, Any> {
        logger.info("Calculating delivery time for storeId: $storeId, userCoordinates: $userCoordinates")
        // 가게 좌표 가져오기
        val storeCoordinates = getStoreCoordinates(storeId)
            ?: throw IllegalArgumentException("Store coordinates not found for storeId=$storeId")

        logger.info("Store coordinates: $storeCoordinates")

        // 거리 계산
        val distance = storeRedisRepository.getDistanceBetweenUserByStore(
            userKey = "user:${userCoordinates.latitude},${userCoordinates.longitude}",
            storeKey = storeId
        )

        logger.info("Calculated distance: $distance km")

        // 거리 범위에 따른 배달 시간 계산
        val deliveryTime = when {
            distance < 5 -> 25
            distance < 10 -> 30
            distance < 20 -> 35
            distance < 30 -> 40
            distance < 40 -> 45
            else -> 60 // 40km 이상
        }

        return mapOf(
            "storeId" to storeId,
            "distance" to String.format("%.2f km", distance),
            "deliveryTime" to "$deliveryTime 분"
        ).also {
            logger.info("Delivery time calculation result: $it")
        }
    }


    private fun getStoreCoordinates(storeId: String): Coordinates? {
        logger.info("Getting store coordinates for storeId: $storeId")
        return storeRedisRepository.getStoreLocation(storeId)?.also { logger.info("Store coordinates found in Redis: $it") }
            ?: storeRepository.findById(storeId)?.let {
                val coordinates = Coordinates(it.latitude, it.longitude)
                logger.info("Store coordinates found in repository: $coordinates")
                storeRedisRepository.saveStoreLocation(storeId, coordinates)
                logger.info("Store coordinates saved to Redis")
                coordinates
            }.also {
                if (it == null) logger.warn("Store coordinates not found for storeId: $storeId")
            }
    }
}
