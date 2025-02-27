package org.fastcampus.applicationclient.order.service

import org.fastcampus.applicationclient.aop.OrderMetered
import org.fastcampus.order.exception.OrderException
import org.fastcampus.order.repository.OrderLockManager
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.payment.service.RefundManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderCancellationService(
    private val orderRepository: OrderRepository,
    private val refundManager: RefundManager,
    private val orderLockManager: OrderLockManager,
) {
    @Transactional
    @OrderMetered
    fun cancelOrder(orderId: String) {
        val order = orderRepository.findById(orderId) ?: throw OrderException.OrderCanNotCancelled(orderId)
        orderLockManager.lock(order.id) {
            order.cancel()
        }
        orderRepository.save(order)
        refundManager.refundOrder(orderId)
    }
}
