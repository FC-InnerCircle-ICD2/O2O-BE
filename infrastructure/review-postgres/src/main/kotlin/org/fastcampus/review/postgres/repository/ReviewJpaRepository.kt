package org.fastcampus.review.postgres.repository

import org.fastcampus.review.postgres.entity.ReviewJpaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface ReviewJpaRepository : JpaRepository<ReviewJpaEntity, Long>, JpaSpecificationExecutor<ReviewJpaEntity> {
    fun findByOrderIdIn(orderIds: List<String>): List<ReviewJpaEntity>

    fun findByUserId(memberId: Long, pageable: Pageable): Page<ReviewJpaEntity>

    @Query(
        value = """
            SELECT  ROUND(AVG(TOTAL_SCORE), 1)
            FROM    REVIEWS
            WHERE   STORE_ID = :storeId
        """,
        nativeQuery = true,
    )
    fun getTotalAverageScoreByStoreId(storeId: String): Double?

    @Query(
        value = """
            SELECT  ROUND(AVG(TASTE_SCORE), 1)
            FROM    REVIEWS
            WHERE   STORE_ID = :storeId
        """,
        nativeQuery = true,
    )
    fun getTasteAverageScoreByStoreId(storeId: String): Double?

    @Query(
        value = """
            SELECT  ROUND(AVG(AMOUNT_SCORE), 1)
            FROM    REVIEWS
            WHERE   STORE_ID = :storeId
        """,
        nativeQuery = true,
    )
    fun getAmountAverageScoreByStoreId(storeId: String): Double?

    fun countByStoreId(storeId: String): Long
}
