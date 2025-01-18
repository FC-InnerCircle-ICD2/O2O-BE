package org.fastcampus.store.mongo.repository

import org.bson.types.ObjectId
import org.fastcampus.store.entity.Store
import org.fastcampus.store.mongo.document.toModel
import org.fastcampus.store.repository.StoreRepository
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Component
class StoreMongoRepositoryImpl(
    @Lazy private val storeMongoRepository: StoreMongoRepository,
) : StoreRepository {
    override fun findByCategory(category: String): List<Store> {
        return storeMongoRepository.findByCategory(category).map { it.toModel() }
    }


    override fun findById(storeId: String): Store? {

        return storeMongoRepository.findById(ObjectId(storeId))
            .map { it.toModel() }
            .orElse(null)
    }
}
