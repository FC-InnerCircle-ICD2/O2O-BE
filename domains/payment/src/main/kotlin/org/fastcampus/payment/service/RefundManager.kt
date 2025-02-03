package org.fastcampus.payment.service

import org.fastcampus.payment.entity.Refund
import org.fastcampus.payment.repository.RefundRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RefundManager(
    private val refundRepository: RefundRepository,
) {
    companion object {
        private val log = LoggerFactory.getLogger(RefundManager::class.java)
    }

    fun refundOrder(orderId: String) {
        val refund = Refund(status = Refund.Status.WAIT, orderId = orderId)
        refundRepository.save(refund)
    }

    @Scheduled(fixedDelay = 60000)
    private fun processFailedRefunds() {
        val failedRefunds = refundRepository.findAllByStatuses(listOf(Refund.Status.WAIT, Refund.Status.FAIL))
        for (refund in failedRefunds) {
            try {
                if (requestRefund(refund)) {
                    refundRepository.save(refund.complete())
                } else {
                    refundRepository.save(refund.fail())
                }
            } catch (e: Exception) {
                log.error("환불 재처리 실패, orderId: ${refund.orderId}")
                refundRepository.save(refund.fail())
            }
        }
    }

    private fun requestRefund(refund: Refund): Boolean {
        // TODO 결제 PG사 연동 관련으로 차후 결제 API 명세서가 나오면 작성 진행
        return true
    }
}
