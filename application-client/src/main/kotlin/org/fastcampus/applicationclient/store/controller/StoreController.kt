package org.fastcampus.applicationclient.store.controller

import org.fastcampus.applicationclient.store.controller.dto.response.StoreDetailsResponse
import org.fastcampus.applicationclient.store.service.StoreService
import org.fastcampus.common.dto.APIResponseDTO
import org.fastcampus.store.entity.TrendKeyword
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
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestHeader("X-User-Lat") userLat: Double,
        @RequestHeader("X-User-Lng") userLng: Double,
    ): ResponseEntity<APIResponseDTO<StoreDetailsResponse>> {
        logger.info("Received request for store details. ID: $id, Lat: $userLat, Lng: $userLng")
        return try {
            val response = storeService.getStoreDetails(id, page, size, Coordinates(userLat, userLng))
            logger.info("Successfully retrieved store details for ID: $id")
            ResponseEntity.ok(APIResponseDTO(200, "ok", response))
        } catch (e: Exception) {
            logger.error("Error retrieving store details for ID: $id", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIResponseDTO(500, "Error retrieving store details", null))
        }
    }

    @GetMapping("/trend")
    fun getTrendKeywords(): List<TrendKeyword>? {
        return storeService.getTrendKeywords()
    }

    @PostMapping("/search")
    fun search(
        @RequestBody keyword: String,
    ) {
        storeService.search(keyword)
    }

    @GetMapping("/test")
    fun getTest(): String {
        return "test"
    }
}
