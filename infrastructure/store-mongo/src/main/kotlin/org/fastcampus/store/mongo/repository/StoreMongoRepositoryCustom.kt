package org.fastcampus.store.mongo.repository

import org.fastcampus.store.entity.Store
import org.fastcampus.store.entity.StoreWithDistance
import org.fastcampus.store.mongo.document.StoreDocument
import org.fastcampus.store.mongo.document.toModel
import org.fastcampus.store.repository.StoreRepository
import org.springframework.data.geo.Metrics
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.NearQuery
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component

@Component
internal class StoreMongoRepositoryCustom(
    private val storeMongoRepository: StoreMongoRepository,
    private val mongoTemplate: MongoTemplate,
) : StoreRepository {
    override fun findByCategory(category: String): List<Store> {
        return storeMongoRepository.findByCategory(category).map { it.toModel() }
    }

    override fun findById(storeId: String): Store? {
        return storeMongoRepository.findById(storeId)
            ?.map { it.toModel() }
            ?.orElse(null)
    }

    override fun findOwnerIdByStoreId(storeId: String): String? {
        val query = Query(Criteria.where("id").`is`(storeId))
            .also { it.fields().include("ownerId").exclude("_id") }

        val result = mongoTemplate.findOne(query, OwnerIdProjection::class.java, "stores")
        return result?.ownerId
    }

    override fun findStoreNearbyAndCondition(
        latitude: Double,
        longitude: Double,
        category: Store.Category?,
        searchName: String?,
        page: Int,
        size: Int,
    ): List<StoreWithDistance>? {
        // 기본 geoNear 쿼리 생성
        val nearQuery = NearQuery.near(longitude, latitude)
            .spherical(true)
            .maxDistance(5.0, Metrics.KILOMETERS)

        // 추가 조건 정의
        val query = Query()

        // 카테고리 조건 추가
        category?.let {
            query.addCriteria(Criteria.where("category").`is`(it.code))
        }

        // 이름 조건 추가
        searchName?.let {
            query.addCriteria(Criteria.where("name").regex(".*$it.*", "i")) // 대소문자 구분 없이 검색
        }

        // 페이지네이션 추가 (skip과 limit)
        val skip = page * size
        query.skip(skip.toLong())
        query.limit(size)

        // NearQuery에 추가 조건을 연결
        nearQuery.query(query)

        // geoNear 실행
        val geoResults = mongoTemplate.geoNear(nearQuery, StoreDocument::class.java)

        // 결과 매핑 및 반환
        return geoResults.map { StoreWithDistance(it.content.toModel(), it.distance.value.toString()) }
    }
}

internal data class OwnerIdProjection(
    val ownerId: String,
)
