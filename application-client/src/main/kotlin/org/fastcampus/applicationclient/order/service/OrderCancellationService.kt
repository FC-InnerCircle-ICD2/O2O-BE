package org.fastcampus.applicationclient.order.service

import org.fastcampus.applicationclient.aop.OrderMetered
import org.fastcampus.applicationclient.order.service.event.OrderCancellationEvent
import org.fastcampus.applicationclient.order.service.event.OrderDetailStatusEvent
import org.fastcampus.order.entity.Order
import org.fastcampus.order.exception.OrderException
import org.fastcampus.order.repository.OrderLockManager
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.payment.service.RefundManager
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderCancellationService(
    private val orderRepository: OrderRepository,
    private val refundManager: RefundManager,
    private val eventPublisher: ApplicationEventPublisher,
    private val orderLockManager: OrderLockManager,
) {
    @Transactional
    @OrderMetered
    fun cancelOrder(orderId: String, userId: Long) {
        val order: Order = orderLockManager.lock(orderId) {
            val order = orderRepository.findById(orderId) ?: throw OrderException.OrderCanNotCancelled(orderId)
            if (order.userId != userId) {
                throw OrderException.NotMatchedUser(orderId, userId)
            }
            order.cancel()
            orderRepository.save(order)
        }
        refundManager.refundOrder(order.id, order.orderPrice, order.paymentId)
        eventPublisher.publishEvent(OrderCancellationEvent(storeId = order.storeId ?: "", orderId = order.id))
        eventPublisher.publishEvent(OrderDetailStatusEvent(orderId, order.status))
    }
}
