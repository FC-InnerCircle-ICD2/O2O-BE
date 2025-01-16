package org.fastcampus.applicationclient.store.controller

import org.fastcampus.applicationclient.store.controller.dto.StoreDetailsResponse
import org.fastcampus.applicationclient.store.service.StoreService
import org.fastcampus.common.dto.APIResponseDTO
import org.fastcampus.store.redis.Coordinates
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by brinst07 on 25. 1. 11..
 */
@RestController
@RequestMapping("/stores")
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
    ): ResponseEntity<APIResponseDTO<StoreDetailsResponse>> {
        logger.info("Received request for store details. ID: $id, Lat: $userLat, Lng: $userLng")
        return try {
            val response = storeService.getStoreDetails(id, Coordinates(userLat, userLng))
            logger.info("Successfully retrieved store details for ID: $id")
            ResponseEntity.ok(APIResponseDTO(200, "ok", response))
        } catch (e: Exception) {
            logger.error("Error retrieving store details for ID: $id", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIResponseDTO(500, "Error retrieving store details", null))
        }
    }

    @GetMapping("/test")
    fun getTest(): String {
        return "test";
    }
}
