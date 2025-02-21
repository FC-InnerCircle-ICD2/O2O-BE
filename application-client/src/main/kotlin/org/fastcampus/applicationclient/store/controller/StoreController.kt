package org.fastcampus.applicationclient.store.controller

import org.fastcampus.applicationclient.store.controller.dto.response.CategoryResponse
import org.fastcampus.applicationclient.store.controller.dto.response.MenuResponse
import org.fastcampus.applicationclient.store.controller.dto.response.StoreInfo
import org.fastcampus.applicationclient.store.controller.dto.response.TrendKeywordsResponse
import org.fastcampus.applicationclient.store.service.StoreService
import org.fastcampus.common.dto.APIResponseDTO
import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.common.dto.CursorDTOString
import org.fastcampus.store.entity.Store
import org.fastcampus.store.redis.Coordinates
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Created by brinst07 on 25. 1. 11..
 */
@RestController
@RequestMapping("/api/v1/stores")
class StoreController(
    private val storeService: StoreService,
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(StoreController::class.java)
    }

    @GetMapping("/{id}")
    fun getStoreDetails(
        @PathVariable id: String,
        @RequestHeader("X-User-Lat") userLat: Double,
        @RequestHeader("X-User-Lng") userLng: Double,
    ): ResponseEntity<APIResponseDTO<StoreInfo>> {
        return try {
            val response = storeService.getStoreInfo(id, Coordinates(userLat, userLng))
            ResponseEntity.ok(APIResponseDTO(HttpStatus.OK.value(), "OK", response))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build()
        }
    }

    @GetMapping("/{storeId}/menus/{menuId}/options")
    fun getMenusOptions(
        @PathVariable storeId: String,
        @PathVariable menuId: String,
    ): APIResponseDTO<MenuResponse> {
        logger.info("Received request for menu options groups. storeId: $storeId menusId: $menuId")
        return try {
            val response = storeService.getMenusOptions(storeId, menuId)
            logger.info("Successfully retrieved all store option groups for storeId: $response")
            APIResponseDTO(HttpStatus.OK.value(), "OK", response)
        } catch (e: Exception) {
            logger.info("Error retrieved all store option groups for storeId: $storeId")
            return APIResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "스토어 메뉴 옵션 조회 실패", null)
        }
    }

    @GetMapping("/{id}/menus")
    fun getCategories(
        @PathVariable id: String,
    ): APIResponseDTO<List<CategoryResponse>> {
        return APIResponseDTO(HttpStatus.OK.value(), "success", storeService.getCategories(id))
    }

    @GetMapping("/suggestion")
    fun getStoreSuggestion(
        @RequestParam(defaultValue = "") affix: String,
    ): ResponseEntity<APIResponseDTO<List<String>>> {
        val response = storeService.getStoreSuggestions(affix)
        return ResponseEntity.ok(APIResponseDTO(HttpStatus.OK.value(), "OK", response))
    }

    @GetMapping("/trend")
    fun getTrendKeywords(): ResponseEntity<APIResponseDTO<TrendKeywordsResponse>>? {
        val response = storeService.getTrendKeywords()
        logger.info("Successfully retrieved trend keywords: $response")
        return ResponseEntity.ok(APIResponseDTO(HttpStatus.OK.value(), "OK", response))
    }

    @PostMapping("/search")
    fun search(
        @RequestBody keyword: String,
    ): ResponseEntity<APIResponseDTO<String>> {
        val addCount = storeService.search(keyword)
        if (addCount) {
            logger.info("Successfully added search keyword: $keyword")
            return ResponseEntity.ok(APIResponseDTO(HttpStatus.OK.value(), "OK", "success"))
        } else {
            logger.info("Failed to add search keyword: $keyword")
            return ResponseEntity.ok(APIResponseDTO(HttpStatus.OK.value(), "OK", "fail"))
        }
    }

    @GetMapping("/list")
    fun getStoresByNearbyByAndCondition(
        @RequestHeader("X-User-Lat") userLat: Double,
        @RequestHeader("X-User-Lng") userLng: Double,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "5") size: Int,
        @RequestParam(required = false) category: Store.Category?,
        @RequestParam(required = false) searchCondition: String?,
    ): ResponseEntity<APIResponseDTO<CursorDTO<StoreInfo>>> {
        return ResponseEntity.ok(
            APIResponseDTO(
                HttpStatus.OK.value(),
                HttpStatus.OK.reasonPhrase,
                storeService.getStoresByNearByAndCondition(userLat, userLng, page, size, category, searchCondition),
            ),
        )
    }

    @GetMapping("/list-cursor")
    fun getStoresByNearbyAndConditionCursor(
        @RequestHeader("X-User-Lat") userLat: Double,
        @RequestHeader("X-User-Lng") userLng: Double,
        @RequestParam(defaultValue = "5") size: Int,
        @RequestParam(required = false) category: Store.Category?,
        @RequestParam(required = false) keyword: String?,
        @RequestParam(required = false) cursor: String?, // "distance_storeId"
//        @RequestParam(required = false) order: OrderType?, // "distance_storeId"
    ): ResponseEntity<APIResponseDTO<CursorDTOString<StoreInfo>>> {
        logger.info("category: $category, searchCondition: $keyword")
        val result = storeService.getStoresByNearByAndConditionCursor(
            latitude = userLat,
            longitude = userLng,
            size = size,
            category = category,
            keyword = keyword,
            cursor = cursor,
        )
        return ResponseEntity.ok(
            APIResponseDTO(
                status = HttpStatus.OK.value(),
                message = "OK",
                data = result,
            ),
        )
    }
}
