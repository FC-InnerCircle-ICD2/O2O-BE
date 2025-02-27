package org.fastcampus.applicationoss.batch.tasklet.payment

import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.store.repository.StoreRepository
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component

/**
 * Created by brinst07 on 25. 2. 27.
 */
@Component
class StoreTasklet (
    private val storeRepository: StoreRepository,
    private val orderRepository: OrderRepository,
) : Tasklet{
    companion object {
        private val log = LoggerFactory.getLogger(RefundTasklet::class.java)
    }

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        val findOrderCnt = orderRepository.findOrderCnt()
        findOrderCnt.forEach { orderCountInfo ->
            try {
                storeRepository.updateOrderCnt(orderCountInfo.storeId, orderCountInfo.orderCnt)
            } catch (e: Exception) {
                log.error("order cnt 업데이트 실패, storeId: ${orderCountInfo.storeId}, orderCnt: ${orderCountInfo.orderCnt}")
            }
        }
        return  RepeatStatus.FINISHED
    }
}
