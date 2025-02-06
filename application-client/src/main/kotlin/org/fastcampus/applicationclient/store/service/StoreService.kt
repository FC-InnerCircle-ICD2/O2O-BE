package org.fastcampus.applicationclient.store.service

import org.fastcampus.applicationclient.aop.StoreMetered
import org.fastcampus.applicationclient.store.controller.dto.response.CategoryResponse
import org.fastcampus.applicationclient.store.controller.dto.response.MenuOptionGroupsResponse
import org.fastcampus.applicationclient.store.controller.dto.response.MenuOptionResponse
import org.fastcampus.applicationclient.store.controller.dto.response.MenuResponse
import org.fastcampus.applicationclient.store.controller.dto.response.StoreInfo
import org.fastcampus.applicationclient.store.controller.dto.response.TrendKeyword
import org.fastcampus.applicationclient.store.controller.dto.response.TrendKeywordsResponse
import org.fastcampus.applicationclient.store.mapper.StoreMapper.toCategoryInfo
import org.fastcampus.applicationclient.store.mapper.StoreMapper.toStoreInfo
import org.fastcampus.applicationclient.store.mapper.calculateDeliveryTime
import org.fastcampus.applicationclient.store.mapper.fetchDistance
import org.fastcampus.applicationclient.store.mapper.fetchStoreCoordinates
import org.fastcampus.applicationclient.store.utils.PaginationUtils.paginate
import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.store.exception.StoreException
import org.fastcampus.store.redis.Coordinates
import org.fastcampus.store.redis.StoreRedisRepository
import org.fastcampus.store.repository.StoreRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

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
    @StoreMetered
    fun getStoreInfo(storeId: String, userCoordinates: Coordinates): StoreInfo =
        storeRepository.findById(storeId)?.run {
            val deliveryInfo = calculateDeliveryDetails(storeId, userCoordinates)
            toStoreInfo(deliveryInfo["deliveryTime"] as String, deliveryInfo["distance"] as Double)
        } ?: throw StoreException.StoreNotFoundException(storeId)

    @Transactional(readOnly = true)
    @StoreMetered
    fun getCategories(storeId: String): List<CategoryResponse> =
        storeRepository.findById(storeId)?.storeMenuCategory?.map { it.toCategoryInfo() }
            ?: throw StoreException.StoreNotFoundException(storeId)

    fun calculateDeliveryDetails(storeId: String, userCoordinates: Coordinates): Map<String, Any> =
        getStoreCoordinates(storeId).let {
            val distance = storeRedisRepository.fetchDistance(userCoordinates, storeId)
            val deliveryTime = distance.calculateDeliveryTime()

            mapOf(
                "storeId" to storeId,
                "distance" to distance, // 원본 거리값 (미터 단위)
                "deliveryTime" to "$deliveryTime 분",
            ).apply {
                logger.info("Delivery time calculation result: $this")
            }
        }

    private fun getStoreCoordinates(storeId: String) = storeRepository.fetchStoreCoordinates(storeId, storeRedisRepository)

    @Transactional(readOnly = true)
    @StoreMetered
    fun getMenusOptions(storeId: String, menuId: String): MenuResponse {
        val menuInfo = storeRepository.findById(storeId)
            ?.storeMenuCategory
            ?.flatMap { it.menu ?: emptyList() }
            ?.find { it.id == menuId }
            ?: throw StoreException.StoreNotFoundException(storeId)
        val response = menuInfo.let { menu ->
            MenuResponse(
                menuId = menu.id ?: "",
                name = menu.name ?: "",
                price = menu.price ?: "0",
                desc = menu.desc ?: "",
                imgUrl = menu.imgUrl ?: "",
                isSoldOut = menu.isSoldOut,
                isBest = Random.nextBoolean(),
                isManyOrder = Random.nextBoolean(),
                menuOptionGroups = menu.menuOptionGroup?.map { optionGroup ->
                    MenuOptionGroupsResponse(
                        id = optionGroup.id ?: "",
                        name = optionGroup.name ?: "",
                        type = if (optionGroup.maxSel!! > 1) "checkbox" else "radio",
                        minSel = optionGroup.minSel?.toLong() ?: 1,
                        maxSel = optionGroup.maxSel?.toLong() ?: 1,
                        options = optionGroup.menuOption?.map { option ->
                            MenuOptionResponse(
                                id = option.id ?: "",
                                name = option.name ?: "",
                                price = option.price ?: 0,
                                isSoldOut = option.isSoldOut,
                            )
                        } ?: emptyList(),
                    )
                } ?: emptyList(),
            )
        }
        return response
    }

    @Transactional(readOnly = true)
    @StoreMetered
    fun getStoreSuggestions(affix: String, page: Int, size: Int): CursorDTO<String> {
        val storeNameList = storeRedisRepository.getSuggestions(affix, page, size) ?: return CursorDTO(emptyList(), null)
        return storeNameList.paginate(page, size)
    }

    @Transactional(readOnly = true)
    @StoreMetered
    fun getTrendKeywords(): TrendKeywordsResponse? {
        val keywords = storeRedisRepository.getTrendKeywords()
        return keywords?.let {
            TrendKeywordsResponse(
                keywords.entries
                    .sortedByDescending { it.value }
                    .mapIndexed { index, map ->
                        TrendKeyword(
                            keyword = map.key,
                            order = index + 1,
                        )
                    },
            )
        }
    }

    @Transactional
    @StoreMetered
    fun search(keyword: String): Boolean {
        val storeNameExist = storeRedisRepository.existsByName(keyword)
        if (storeNameExist == true) { // 가게 이름이 검색어와 일치하는 가게가 있을 경우
            storeRedisRepository.addSearch(keyword)
            return true
        } else {
            return false
        }
    }
}
