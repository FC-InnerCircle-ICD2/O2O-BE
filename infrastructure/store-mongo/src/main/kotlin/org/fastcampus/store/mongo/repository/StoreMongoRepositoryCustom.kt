package org.fastcampus.store.mongo.repository

import org.fastcampus.store.entity.Store
import org.fastcampus.store.mongo.document.toModel
import org.fastcampus.store.repository.StoreRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
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

    override fun existsByName(name: String): Boolean? {
        return storeMongoRepository.existsStoreDocumentByName(name)
    }
}

internal data class OwnerIdProjection(
    val ownerId: String,
)
