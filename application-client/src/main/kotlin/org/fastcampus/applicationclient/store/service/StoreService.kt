package org.fastcampus.applicationclient.store.service

import org.fastcampus.applicationclient.store.controller.dto.response.CategoryInfo
import org.fastcampus.applicationclient.store.controller.dto.response.MenuInfo
import org.fastcampus.applicationclient.store.controller.dto.response.MenuOptionGroupsResponse
import org.fastcampus.applicationclient.store.controller.dto.response.MenuOptionInfo
import org.fastcampus.applicationclient.store.controller.dto.response.StoreDetailsResponse
import org.fastcampus.applicationclient.store.controller.dto.response.StoreInfo
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
    fun getStoreDetails(storeId: String, page: Int, size: Int, userCoordinates: Coordinates): StoreDetailsResponse {
        logger.info("Fetching store details for storeId: $storeId, userCoordinates: $userCoordinates")
        val store = storeRepository.findById(storeId)
            ?: throw IllegalArgumentException("Store not found: $storeId")
        val deliveryInfo = calculateDeliveryTime(storeId, userCoordinates)

        // 페이징 처리
        val categories = store.storeMenuCategory?.mapIndexed { index, category ->
            val totalElements = category.menu?.size?.toLong() ?: 0L
            val totalPages = if (totalElements > 0) ((totalElements + size - 1) / size).toInt() else 0
            val startIndex = page * size
            val endIndex = (startIndex + size).coerceAtMost(category.menu?.size ?: 0)

            CategoryInfo(
                categoryId = index + 1,
                categoryName = category.name ?: "Unknown",
                menus = category.menu?.subList(startIndex, endIndex)?.map { menu ->
                    MenuInfo(
                        id = menu.id ?: "",
                        name = menu.name ?: "Unknown",
                        price = "${menu.price ?: 0}원",
                        description = menu.desc ?: "",
                        imageUrl = menu.imgUrl ?: "",
                        isSoldOut = menu.isSoldOut,
                    )
                } ?: emptyList(),
                page = page,
                size = size,
                totalPages = totalPages,
                totalElements = totalElements,
            )
        } ?: emptyList()

        return StoreDetailsResponse(
            store = StoreInfo(
                // id = store._id ?: "",
                id = store.id ?: "",
                name = store.name ?: "",
                imageMain = store.imageMain ?: "",
                rating = 4.5,
                reviewCount = 150,
                deliveryTime = deliveryInfo["deliveryTime"] as String,
                freeDelivery = (store.border ?: 0) > 5000,
            ),
            categories = categories,
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
            storeKey = storeId,
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
            "deliveryTime" to "$deliveryTime 분",
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

    fun findAllMenuOptionGroup(storeId: String, menuId: String): List<MenuOptionGroupsResponse> {
        logger.info("call findAllMenuOptionGroup")
        val menuOptionGroupInfo = storeRepository.findById(storeId)
            ?.storeMenuCategory
            ?.flatMap { it.menu ?: emptyList() }
            ?.filter { it.id == menuId }
            ?.flatMap { it.menuOptionGroup ?: emptyList() }
        // logger.info(menuOptionGroupInfo.toString())
        val storeInfo = storeRepository.findById(storeId)
        logger.info("storeInfo: ${storeInfo.toString()}")
        val storeMenuCateInfo = storeInfo?.storeMenuCategory ?: emptyList()
        logger.info("storeMenuCateInfo: $storeMenuCateInfo")
        val menuInfo = storeMenuCateInfo
            .flatMap { it.menu ?: emptyList() }
            .filter { it.id?.equals(menuId, ignoreCase = true) == true }
        logger.info("Filtered menuInfo: $menuInfo")

        val menuGroupInfo = menuInfo.flatMap { it.menuOptionGroup ?: emptyList() }
        logger.info("menuGroupInfo: $menuGroupInfo")

        val response = menuOptionGroupInfo?.map { menuOptionGroup ->
            MenuOptionGroupsResponse(
                id = menuOptionGroup.id ?: "",
                name = menuOptionGroup.name ?: "",
                minSel = menuOptionGroup.minSel?.toString() ?: "0",
                maxSel = menuOptionGroup.maxSel?.toString() ?: "0",
                order = menuOptionGroup.order ?: 0L,
                menuOptions = menuOptionGroup.menuOption?.map { menu ->
                    MenuOptionInfo(
                        id = menu.id ?: "",
                        name = menu.name ?: "",
                        price = "${menu.price}원" ?: "0",
                        isSoldOut = menu.isSoldOut,
                        order = menu.order ?: 0L
                    )
                } ?: emptyList()
            )
        } ?: emptyList()
        return response
    }
}
