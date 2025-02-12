package org.fastcampus.cart.repository

import org.bson.types.ObjectId
import org.fastcampus.cart.document.CartDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface CartMongoRepository : MongoRepository<CartDocument, ObjectId> {
    fun findOneByUserId(userId: Long): CartDocument?
}
