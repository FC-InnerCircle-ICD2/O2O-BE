package org.fastcampus.store.mongo.repository

import org.fastcampus.store.entity.Store
import org.fastcampus.store.mongo.document.toModel
import org.fastcampus.store.repository.StoreRepository

class StoreMongoRepositoryImpl(
    private val storeMongoRepository: StoreMongoRepository,
) : StoreRepository {
    override fun findByCategory(category: String): List<Store> {
        return storeMongoRepository.findByCategory(category).map { it.toModel() }
    }
}
