package org.fastcampus.applicationclient.store.controller

import org.fastcampus.applicationclient.store.controller.dto.response.CategoryInfo
import org.fastcampus.applicationclient.store.controller.dto.response.MenuOptionGroupsResponse
import org.fastcampus.applicationclient.store.controller.dto.response.StoreInfo
import org.fastcampus.applicationclient.store.controller.dto.response.StoreDetailsResponse
import org.fastcampus.applicationclient.store.controller.dto.response.TrendKeywordsResponse
import org.fastcampus.applicationclient.store.service.StoreService
import org.fastcampus.common.dto.APIResponseDTO
import org.fastcampus.common.dto.CursorDTO
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
    ): APIResponseDTO<List<MenuOptionGroupsResponse>> {
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
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "5") size: Int,
    ): ResponseEntity<CursorDTO<CategoryInfo>> {
        return ResponseEntity.ok(storeService.getCategories(id, page, size))
    }

    @GetMapping("/suggestion")
    fun getStoreSuggestion(
        @RequestParam(defaultValue = "") affix: String,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "5") size: Int,
    ): ResponseEntity<CursorDTO<String>> {
        return ResponseEntity.ok(storeService.getStoreSuggestions(affix, page, size))
    }

    @GetMapping("/trend")
    fun getTrendKeywords(): TrendKeywordsResponse? {
        return storeService.getTrendKeywords()
    }

    @PostMapping("/search")
    fun search(
        @RequestBody keyword: String,
    ) {
        storeService.search(keyword)
    }
}
