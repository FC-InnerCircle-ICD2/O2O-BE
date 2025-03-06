package org.fastcampus.applicationoss.batch.tasklet.payment

import org.fastcampus.payment.entity.Refund
import org.fastcampus.payment.exception.RefundException
import org.fastcampus.payment.gateway.PaymentGatewayResponse
import org.fastcampus.payment.repository.PaymentRepository
import org.fastcampus.payment.repository.RefundRepository
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

//@Component
class RefundTasklet(
    private var refundRepository: RefundRepository,
    private val paymentGatewayFactory: PaymentGatewayFactory,
    private val paymentRepository: PaymentRepository,
) : Tasklet {
    companion object {
        private val log = LoggerFactory.getLogger(RefundTasklet::class.java)
    }

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val refundTargets = refundRepository.findAllByStatuses(listOf(Refund.Status.WAIT, Refund.Status.FAIL))
        refundTargets.parallelStream().forEach { refund ->
            try {
                val updatedRefund = if (requestRefund(refund)) {
                    log.info("환불 재처리 성공, orderId: ${refund.orderId}")
                    refund.complete()
                } else {
                    log.error("환불 재처리 실패, orderId: ${refund.orderId}")
                    refund.fail()
                }
                refundRepository.save(updatedRefund)
            } catch (e: Exception) {
                log.error("환불 재처리 실패, orderId: ${refund.orderId}")
                refundRepository.save(refund.fail())
            }
        }
        return RepeatStatus.FINISHED
    }

    private fun requestRefund(refund: Refund): Boolean {
        val payment = paymentRepository.findById(refund.paymentId) ?: throw RefundException.PaymentNotFound(refund.paymentId)
        payment.pgKey?.let {
            val paymentGateway = paymentGatewayFactory.getPaymentGateway(payment.type)
            val response = paymentGateway.cancel(it, refund.orderId, refund.orderPrice)
            return response.status == PaymentGatewayResponse.Status.CANCELED
        }
        return false
    }
}
