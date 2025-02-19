package org.fastcampus.favorite.postgres.repository

import org.fastcampus.favorite.entity.Favorite
import org.fastcampus.favorite.postgres.entity.toModel
import org.fastcampus.favorite.repository.FavoriteRepository
import org.springframework.stereotype.Repository

@Repository
class FavoriteJpaRepositoryCustom(
    private val favoriteJpaRepository: FavoriteJpaRepository,
) : FavoriteRepository {
    override fun addFavorite(favorite: Favorite) {
        favoriteJpaRepository.addFavorite(favorite.userId, favorite.storeId)
    }

    override fun removeFavoriteByUserIdAndStoreId(userId: Long, storeId: String) {
        favoriteJpaRepository.removeByUserIdAndStoreId(userId, storeId)
    }

    override fun findAllFavorite(userId: Long): List<Favorite> {
        return favoriteJpaRepository.findAllByUserId(userId).map { it.toModel() }
    }
}
