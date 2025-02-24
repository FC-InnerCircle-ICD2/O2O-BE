package org.fastcampus.order.mongo.repository

import org.fastcampus.order.entity.Order
import org.fastcampus.order.mongo.document.toJpaDocument
import org.fastcampus.order.mongo.document.toModel
import org.fastcampus.order.repository.OrderDetailRepository
import org.springframework.stereotype.Component

@Component
internal class OrderMongoRepositoryCustom(
    private val orderMongoRepository: OrderMongoRepository,
) : OrderDetailRepository {
    override fun saveOrder(order: Order, paymentType: Map<String, String>): Order {
        return orderMongoRepository.save(order.toJpaDocument(order.id, paymentType)).toModel()
    }

    override fun findById(orderId: String): Order? {
        return orderMongoRepository.findByOrderId(orderId)
            ?.map { it.toModel() }
            ?.orElse(null)
    }
}
