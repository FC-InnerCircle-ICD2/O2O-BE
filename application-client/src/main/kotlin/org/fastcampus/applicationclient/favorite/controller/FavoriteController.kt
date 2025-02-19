package org.fastcampus.applicationclient.favorite.controller

import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.config.security.dto.JwtAuthenticated
import org.fastcampus.applicationclient.favorite.controller.dto.response.FavoriteResponse
import org.fastcampus.applicationclient.favorite.service.FavoriteService
import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/favorites")
class FavoriteController(
    private val favoriteService: FavoriteService,
) {
    @JwtAuthenticated
    @PostMapping("/{storeId}")
    fun addFavorite(
        @AuthenticationPrincipal authMember: AuthMember,
        @PathVariable storeId: String,
    ): APIResponseDTO<Void> {
        favoriteService.addFavorite(authMember.id, storeId)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, null)
    }

    @JwtAuthenticated
    @DeleteMapping("/{storeId}")
    fun removeFavorite(
        @AuthenticationPrincipal authMember: AuthMember,
        @PathVariable storeId: String,
    ): APIResponseDTO<Void> {
        favoriteService.removeFavorite(authMember.id, storeId)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, null)
    }

    @JwtAuthenticated
    @GetMapping
    fun getFavorites(
        @AuthenticationPrincipal authMember: AuthMember,
    ): APIResponseDTO<List<FavoriteResponse>> {
        val response = favoriteService.findAllFavorites(authMember.id)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, response)
    }

    @GetMapping("/recent")
    fun getAllByStoreIdIn(
        @RequestParam storeIds: List<String>,
    ): APIResponseDTO<List<FavoriteResponse>> {
        val response = favoriteService.findAllByStoreIdIn(storeIds)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, response)
    }
}
