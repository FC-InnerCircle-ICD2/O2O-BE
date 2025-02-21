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
import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.common.dto.CursorDTOString
import org.fastcampus.review.repository.ReviewRepository
import org.fastcampus.store.entity.Store
import org.fastcampus.store.exception.StoreException
import org.fastcampus.store.redis.Coordinates
import org.fastcampus.store.redis.StoreRedisRepository
import org.fastcampus.store.repository.StoreRepository
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
    private val reviewRepository: ReviewRepository,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(StoreService::class.java)
    }

    @Transactional(readOnly = true)
    @StoreMetered
    fun getStoreInfo(storeId: String, userCoordinates: Coordinates): StoreInfo =
        storeRepository.findById(storeId)?.run {
            val rating = reviewRepository.getTotalAverageScoreByStoreId(storeId)
            val reviewCount = reviewRepository.countReviewCountByStoreId(storeId).toInt()
            val deliveryInfo = calculateDeliveryDetails(storeId, userCoordinates)
            toStoreInfo(deliveryInfo["deliveryTime"] as String, deliveryInfo["distance"] as Double, rating, reviewCount)
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
                isBest = menu.isBest,
                isManyOrder = menu.isManyOrder,
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
    fun getStoreSuggestions(affix: String): List<String> {
        return storeRedisRepository.getSuggestions(affix)?.take(5) ?: return emptyList()
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

    @StoreMetered
    fun getStoresByNearByAndCondition(
        latitude: Double,
        longitude: Double,
        page: Int,
        size: Int,
        category: Store.Category?,
        searchCondition: String?,
    ): CursorDTO<StoreInfo>? {
        val mapContent = storeRepository
            .findStoreNearbyAndCondition(
                latitude,
                longitude,
                category,
                searchCondition,
                page,
                size,
            )
        val content = mapContent
            .first
            .map {
                val dist = it.distance.toDoubleOrNull() ?: 0.0
                val rating = reviewRepository.getTotalAverageScoreByStoreId(it.store.id ?: "")
                val reviewCount = reviewRepository.countReviewCountByStoreId(it.store.id ?: "").toInt()
                it.store.toStoreInfo(dist, rating, reviewCount)
            }
        return CursorDTO(content, mapContent.second)
    }

    @StoreMetered
    fun getStoresByNearByAndConditionCursor(
        latitude: Double,
        longitude: Double,
        size: Int,
        category: Store.Category?,
        searchCondition: String?,
        cursor: String?, // "distance_storeId" 형태
    ): CursorDTOString<StoreInfo>? {
        // 1) cursor 파싱 (ex: "13.23_STORE1234")
        val (cursorDistance, cursorStoreId) = parseCursor(cursor)

        val (storeList, nextCursor) = storeRepository.findStoreNearByAndConditionWithCursor(
            latitude = latitude,
            longitude = longitude,
            category = category,
            searchName = searchCondition,
            cursorDistance = cursorDistance,
            cursorStoreId = cursorStoreId,
            size = size,
        )

        // 3) 결과를 StoreInfo로 변환
        val content = storeList.map {
            val dist = it.distance.toDoubleOrNull() ?: 0.0
            val rating = reviewRepository.getTotalAverageScoreByStoreId(it.store.id ?: "")
            val reviewCount = reviewRepository.countReviewCountByStoreId(it.store.id ?: "").toInt()
            it.store.toStoreInfo(dist, rating, reviewCount)
        }

        return CursorDTOString(
            content = content,
            nextCursor = nextCursor, // "13.23_STORE1234" etc
            totalCount = content.size.toLong(),
        )
    }

    /**
     * "distance_storeId" 형태의 커서를 파싱해,
     * (Double?, String?) 형태로 반환
     */
    private fun parseCursor(cursor: String?): Pair<Double?, String?> {
        if (cursor.isNullOrBlank()) {
            return Pair(null, null)
        }
        val parts = cursor.split("_")
        if (parts.size != 2) {
            return Pair(null, null)
        }
        val distance = parts[0].toDoubleOrNull()
        val storeId = parts[1]
        return Pair(distance, storeId)
    }
}
