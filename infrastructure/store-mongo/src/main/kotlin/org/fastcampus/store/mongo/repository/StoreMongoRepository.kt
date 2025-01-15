package org.fastcampus.store.mongo.repository

import org.fastcampus.store.mongo.document.StoreDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface StoreMongoRepository: MongoRepository<StoreDocument, Long> {
    fun findByCategory(category: String): List<StoreDocument>
}
