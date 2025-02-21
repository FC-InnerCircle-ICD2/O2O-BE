package org.fastcampus.favorite.postgres.repository

import org.fastcampus.favorite.postgres.entity.FavoriteJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface FavoriteJpaRepository : JpaRepository<FavoriteJpaEntity, Long> {
    @Modifying
    @Query(
        value = """
            INSERT INTO FAVORITES (USER_ID, STORE_ID)
            VALUES (:userId, :storeId)
            ON CONFLICT (USER_ID, STORE_ID) DO NOTHING
        """,
        nativeQuery = true,
    )
    fun addFavorite(userId: Long, storeId: String)

    fun removeByUserIdAndStoreId(userId: Long, storeId: String)

    fun findAllByUserId(userId: Long): List<FavoriteJpaEntity>
}
