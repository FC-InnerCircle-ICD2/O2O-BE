package org.fastcampus.order.repository

import org.fastcampus.order.document.toJpaDocument
import org.fastcampus.order.document.toModel
import org.fastcampus.order.entity.OrderDetail
import org.springframework.stereotype.Component

@Component
class OrderDetailMongoRepositoryCustom(
    private val orderDetailMongoRepository: OrderDetailMongoRepository,
) : OrderDetailRepository {
    override fun saveOrderDetail(orderDetail: OrderDetail): OrderDetail {
        return orderDetailMongoRepository.save(orderDetail.toJpaDocument()).toModel()
    }

    override fun findById(orderId: String): OrderDetail? {
        return orderDetailMongoRepository.findByOrderId(orderId)
            ?.map { it.toModel() }
            ?.orElse(null)
    }
}
