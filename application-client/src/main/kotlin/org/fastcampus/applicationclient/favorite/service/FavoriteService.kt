package org.fastcampus.applicationclient.favorite.service

import org.fastcampus.applicationclient.favorite.controller.dto.response.FavoriteResponse
import org.fastcampus.favorite.entity.Favorite
import org.fastcampus.favorite.repository.FavoriteRepository
import org.fastcampus.review.repository.ReviewRepository
import org.fastcampus.store.repository.StoreRepository
import org.springframework.stereotype.Service

@Service
class FavoriteService(
    private val favoriteRepository: FavoriteRepository,
    private val storeRepository: StoreRepository,
    private val reviewRepository: ReviewRepository,
) {
    fun addFavorite(userId: Long, storeId: String) {
        favoriteRepository.addFavorite(Favorite(userId = userId, storeId = storeId))
    }

    fun removeFavorite(userId: Long, storeId: String) {
        favoriteRepository.removeFavoriteByUserIdAndStoreId(userId, storeId)
    }

    fun findAllFavorites(userId: Long): List<FavoriteResponse> {
        return favoriteRepository.findAllFavorite(userId)
            .map { createFavoriteResponse(it.storeId) }
    }

    fun findAllByStoreIdIn(storeIds: List<String>): List<FavoriteResponse> {
        return storeIds.map { createFavoriteResponse(it) }
    }

    private fun createFavoriteResponse(storeId: String): FavoriteResponse {
        val storeInfo = storeRepository.findById(storeId)
        return FavoriteResponse(
            id = storeInfo?.id ?: "",
            name = storeInfo?.name ?: "",
            imageMain = storeInfo?.imageMain ?: "",
            rating = reviewRepository.getTotalAverageScoreByStoreId(storeId = storeInfo?.id ?: ""),
            reviewCount = reviewRepository.countReviewCountByStoreId(storeId = storeInfo?.id ?: ""),
            minimumOrderAmount = storeInfo?.minimumOrderAmount ?: 0,
        )
    }
}
