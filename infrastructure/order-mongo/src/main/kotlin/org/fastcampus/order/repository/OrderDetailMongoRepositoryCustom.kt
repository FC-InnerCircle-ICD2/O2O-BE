package org.fastcampus.order.repository

import org.fastcampus.order.document.OrderDetailDocument
import org.fastcampus.order.document.toJpaDocument
import org.fastcampus.order.document.toModel
import org.fastcampus.order.entity.OrderDetail
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component

@Component
class OrderDetailMongoRepositoryCustom(
    private val orderDetailMongoRepository: OrderDetailMongoRepository,
    private val mongoTemplate: MongoTemplate,
) : OrderDetailRepository {
    override fun saveOrderDetail(orderDetail: OrderDetail): OrderDetail {
        return orderDetailMongoRepository.save(orderDetail.toJpaDocument()).toModel()
    }

    override fun findById(orderId: String): OrderDetail? {
        return orderDetailMongoRepository.findByOrderId(orderId)
            ?.map { it.toModel() }
            ?.orElse(null)
    }

    override fun updateStatus(orderId: String, status: Map<String, String>) {
        val query = Query(Criteria.where("orderId").`is`(orderId))
        val update = Update()
            .set("status", status)
        mongoTemplate.updateFirst(query, update, OrderDetailDocument::class.java)
    }
}
