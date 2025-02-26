package org.fastcampus.order.repository

import org.bson.types.ObjectId
import org.fastcampus.order.document.OrderDetailDocument
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface OrderDetailMongoRepository : MongoRepository<OrderDetailDocument, ObjectId> {
    fun findByOrderId(orderId: String): Optional<OrderDetailDocument>?
}
