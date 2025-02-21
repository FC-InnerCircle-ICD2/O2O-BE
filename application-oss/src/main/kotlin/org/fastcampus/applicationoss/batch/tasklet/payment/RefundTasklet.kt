package org.fastcampus.applicationoss.batch.tasklet.payment

import org.fastcampus.payment.entity.Refund
import org.fastcampus.payment.repository.RefundRepository
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component

@Component
class RefundTasklet(
    private var refundRepository: RefundRepository,
) : Tasklet {
    companion object {
        private val log = LoggerFactory.getLogger(RefundTasklet::class.java)
    }

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
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
        return RepeatStatus.FINISHED
    }

    private fun requestRefund(refund: Refund): Boolean {
        // TODO 결제 PG사 연동 관련으로 차후 결제 API 명세서가 나오면 작성 진행
        log.info(refund.toString())
        return true
    }
}
