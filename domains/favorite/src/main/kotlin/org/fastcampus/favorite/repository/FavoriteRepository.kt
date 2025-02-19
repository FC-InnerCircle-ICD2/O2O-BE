package org.fastcampus.favorite.repository

import org.fastcampus.favorite.entity.Favorite

interface FavoriteRepository {
    fun addFavorite(favorite: Favorite)

    fun removeFavoriteByUserIdAndStoreId(userId: Long, storeId: String)

    fun findAllFavorite(userId: Long): List<Favorite>
}
