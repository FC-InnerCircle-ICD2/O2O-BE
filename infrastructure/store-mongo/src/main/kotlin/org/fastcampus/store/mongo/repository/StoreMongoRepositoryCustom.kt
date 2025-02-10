package org.fastcampus.store.mongo.repository

import org.fastcampus.store.entity.Store
import org.fastcampus.store.entity.StoreWithDistance
import org.fastcampus.store.mongo.document.StoreDocument
import org.fastcampus.store.exception.StoreException
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

    override fun findByOwnerId(ownerId: String): String? {
        val store = storeMongoRepository.findByOwnerId(ownerId) ?: throw StoreException.StoreNotFoundException(ownerId)
        return store.id
    }

    override fun findStoreNearbyAndCondition(
        latitude: Double,
        longitude: Double,
        category: Store.Category?,
        searchName: String?,
        page: Int,
        size: Int,
    ): Pair<List<StoreWithDistance>, Boolean> { // 결과와 다음 페이지 존재 여부를 반환
        val nearQuery = NearQuery.near(longitude, latitude)
            .spherical(true)
            .maxDistance(5.0, Metrics.KILOMETERS)

        val query = Query()

        category?.let {
            query.addCriteria(Criteria.where("category").`is`(it.code))
        }

        searchName?.let {
            query.addCriteria(Criteria.where("name").regex(".*$it.*", "i"))
        }

        val skip = page * size
        query.skip(skip.toLong())
        query.limit(size + 1) // 요청 크기보다 하나 더 가져옴

        nearQuery.query(query)

        val geoResults = mongoTemplate.geoNear(nearQuery, StoreDocument::class.java)

        // `size + 1`개 데이터를 가져오고, 마지막 데이터를 제외한 리스트 반환
        val stores = geoResults.map { StoreWithDistance(it.content.toModel(), it.distance.value.toString()) }
        val hasNext = stores.size > size // 가져온 데이터가 요청한 size보다 크다면 다음 페이지가 있음
        val content = stores.take(size) // 실제 반환할 데이터는 요청한 size만큼

        return Pair(content, hasNext)
    }
}

internal data class OwnerIdProjection(
    val ownerId: String,
)
