package org.fastcampus.payment.service

import org.fastcampus.payment.entity.Refund
import org.fastcampus.payment.repository.RefundRepository
import org.springframework.stereotype.Component

@Component
class RefundManager(
    private val refundRepository: RefundRepository,
) {
    fun refundOrder(orderId: String) {
        val refund = Refund(status = Refund.Status.WAIT, orderId = orderId)
        refundRepository.save(refund)
    }
}
