package org.fastcampus.favorite.postgres.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.fastcampus.common.entity.BaseEntity
import org.fastcampus.favorite.entity.Favorite

@Entity
@Table(
    name = "FAVORITES",
    indexes = [Index(name = "FAVORITE_IDX_1", columnList = "USER_ID, STORE_ID", unique = true)],
)
class FavoriteJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "USER_ID", nullable = false)
    val userId: Long,
    @Column(name = "STORE_ID", nullable = false)
    val storeId: String,
) : BaseEntity()

fun FavoriteJpaEntity.toModel() =
    Favorite(
        id = this.id,
        userId = this.userId,
        storeId = this.storeId,
    )

fun Favorite.toEntity() =
    FavoriteJpaEntity(
        userId = this.userId,
        storeId = this.storeId,
    )
