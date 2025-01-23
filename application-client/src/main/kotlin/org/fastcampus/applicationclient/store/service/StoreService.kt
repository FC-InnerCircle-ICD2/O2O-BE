package org.fastcampus.applicationclient.store.service

import org.fastcampus.applicationclient.store.controller.dto.response.CategoryInfo
import org.fastcampus.applicationclient.store.controller.dto.response.MenuOptionGroupsResponse
import org.fastcampus.applicationclient.store.controller.dto.response.MenuOptionResponse
import org.fastcampus.applicationclient.store.controller.dto.response.StoreInfo
import org.fastcampus.applicationclient.store.mapper.StoreMapper.toCategoryInfo
import org.fastcampus.applicationclient.store.mapper.StoreMapper.toStoreInfo
import org.fastcampus.applicationclient.store.mapper.calculateDeliveryTime
import org.fastcampus.applicationclient.store.mapper.fetchDistance
import org.fastcampus.applicationclient.store.mapper.fetchStoreCoordinates
import org.fastcampus.applicationclient.store.utils.PaginationUtils.paginate
import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.store.exception.StoreException
import org.fastcampus.store.entity.Store
import org.fastcampus.store.entity.StoreWithDistance
import org.fastcampus.store.redis.Coordinates
import org.fastcampus.store.redis.StoreRedisRepository
import org.fastcampus.store.repository.StoreRepository
import org.fastcampus.store.utils.DeliveryUtils
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
        private val logger = LoggerFactory.getLogger(StoreService::class.java)
    }

    @Transactional(readOnly = true)
    fun getStoreInfo(storeId: String, userCoordinates: Coordinates): StoreInfo =
        storeRepository.findById(storeId)?.run {
            val deliveryInfo = calculateDeliveryDetails(storeId, userCoordinates)
            toStoreInfo(deliveryInfo["deliveryTime"] as String)
        } ?: throw StoreException.StoreNotFoundException(storeId)

    @Transactional(readOnly = true)
    fun getCategories(storeId: String, page: Int, size: Int): CursorDTO<CategoryInfo> =
        storeRepository.findById(storeId)?.storeMenuCategory?.map { it.toCategoryInfo() }
            ?.paginate(page, size)
            ?: throw StoreException.StoreNotFoundException(storeId)

    fun calculateDeliveryDetails(storeId: String, userCoordinates: Coordinates): Map<String, Any> =
        getStoreCoordinates(storeId).let {
            val distance = storeRedisRepository.fetchDistance(userCoordinates, storeId)
            val deliveryTime = distance.calculateDeliveryTime()

            mapOf(
                "storeId" to storeId,
                "distance" to "%.2f km".format(distance),
                "deliveryTime" to "$deliveryTime 분",
            ).apply {
                logger.info("Delivery time calculation result: $this")
            }
        }

    private fun getStoreCoordinates(storeId: String) = storeRepository.fetchStoreCoordinates(storeId, storeRedisRepository)

    fun getMenusOptions(storeId: String, menuId: String): List<MenuOptionGroupsResponse> {
        logger.info("Call findAllMenuOptionGroup storeId: $storeId, menuId: $menuId")
        val test = storeRepository.findById(storeId)
        logger.info(test.toString())

        val menuOptionGroupInfo = storeRepository.findById(storeId)
            ?.storeMenuCategory
            ?.flatMap { it.menu ?: emptyList() }
            ?.filter { it.id == menuId }
            ?.flatMap { it.menuOptionGroup ?: emptyList() }
            ?: throw IllegalArgumentException("Store id: $storeId menu id: $menuId not found")
        val response = menuOptionGroupInfo.map { menuOptionGroup ->
            MenuOptionGroupsResponse(
                id = menuOptionGroup.id ?: "",
                name = menuOptionGroup.name ?: "",
                minSel = menuOptionGroup.minSel?.toString() ?: "0",
                maxSel = menuOptionGroup.maxSel?.toString() ?: "0",
                order = menuOptionGroup.order ?: 0L,
                menuOptions = menuOptionGroup.menuOption?.map { menu ->
                    MenuOptionResponse(
                        id = menu.id ?: "",
                        name = menu.name ?: "",
                        price = "${menu.price}원",
                        isSoldOut = menu.isSoldOut,
                        order = menu.order ?: 0L,
                    )
                } ?: emptyList(),
            )
        }
        return response
    }

    fun getStoresByNearByAndCondition(
        latitude: Double,
        longitude: Double,
        category: Store.Category?,
        searchCondition: String?,
    ): List<StoreWithDistance>? {
        return storeRepository.findStoreNearbyAndCondition(latitude,longitude, category, searchCondition);
    }
}
