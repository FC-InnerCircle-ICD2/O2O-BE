package org.fastcampus.applicationclient.order.service

import org.fastcampus.applicationclient.aop.OrderMetered
import org.fastcampus.applicationclient.order.service.event.OrderDetailStatusEvent
import org.fastcampus.applicationclient.order.service.event.OrderCancellationEvent
import org.fastcampus.order.exception.OrderException
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
) {
    @Transactional
    @OrderMetered
    fun cancelOrder(orderId: String) {
        val order = orderRepository.findById(orderId) ?: throw OrderException.OrderCanNotCancelled(orderId)
        order.cancel()
        orderRepository.save(order)
        refundManager.refundOrder(orderId)

        eventPublisher.publishEvent(OrderCancellationEvent(storeId = order.storeId ?: "", orderId = order.id))
        eventPublisher.publishEvent(OrderDetailStatusEvent(orderId, order.status))
    }
}
