package org.fastcampus.applicationclient.store.controller

import org.fastcampus.applicationclient.store.controller.dto.response.CategoryInfo
import org.fastcampus.applicationclient.store.controller.dto.response.StoreInfo
import org.fastcampus.applicationclient.store.service.StoreService
import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.store.redis.Coordinates
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
    ): ResponseEntity<StoreInfo> {
        logger.info("Received request for store details. ID: $id, Lat: $userLat, Lng: $userLng")
        return try {
            val response = storeService.getStoreInfo(id, Coordinates(userLat, userLng))
            logger.info("Successfully retrieved store details for ID: $id")
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            logger.error("Error retrieving store details for ID: $id", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build()
        }
    }

    @GetMapping("/{id}/categories")
    fun getCategories(
        @PathVariable id: String,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "5") size: Int,
    ): ResponseEntity<CursorDTO<CategoryInfo>> {
        return ResponseEntity.ok(storeService.getCategories(id, page, size))
    }
}
