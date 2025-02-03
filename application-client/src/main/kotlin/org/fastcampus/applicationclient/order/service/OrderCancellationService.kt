package org.fastcampus.applicationclient.order.service

import org.fastcampus.order.exception.OrderException
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.payment.service.RefundManager
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderCancellationService(
    private val orderRepository: OrderRepository,
    private val refundManager: RefundManager,
) {
    @Transactional
    fun cancelOrder(orderId: String) {
        cancelOrderWithRetry(orderId)
        refundManager.refundOrder(orderId)
    }

    // 주문취소는 주문수락, 주문거절에 대해서 race condition 이 발생하기에 낙관적락으로 동시성 이슈 처리
    @Retryable(
        value = [OptimisticLockingFailureException::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 1000),
        recover = "recoverRaceConditionOnOrder",
    )
    private fun cancelOrderWithRetry(orderId: String) {
        val order = orderRepository.findById(orderId) ?: throw OrderException.OrderCanNotCancelled(orderId)
        order.cancel()
        orderRepository.save(order)
    }

    @Recover
    private fun recoverRaceConditionOnOrder(orderId: String) {
        throw OrderException.OrderCanNotCancelled(orderId)
    }
}
