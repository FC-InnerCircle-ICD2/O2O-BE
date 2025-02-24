package org.fastcampus.order.mongo.repository

import org.bson.types.ObjectId
import org.fastcampus.order.mongo.document.OrderDocument
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface OrderMongoRepository : MongoRepository<OrderDocument, ObjectId> {
    fun findByOrderId(orderId: String): Optional<OrderDocument>?
}
