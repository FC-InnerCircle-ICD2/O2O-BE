package org.fastcampus.store.mongo.repository

import org.bson.types.ObjectId
import org.fastcampus.store.mongo.document.StoreDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface StoreMongoRepository : MongoRepository<StoreDocument, ObjectId> {
    fun findByCategory(category: String): List<StoreDocument>
}
