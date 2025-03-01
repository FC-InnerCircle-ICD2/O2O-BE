package org.fastcampus.applicationclient.order.service.event

import org.fastcampus.order.repository.OrderDetailRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderDetailSaveEventHandler(
    private val orderDetailRepository: OrderDetailRepository,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(OrderDetailSaveEventHandler::class.java)
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleEvent(event: OrderDetailSaveEvent) {
        orderDetailRepository.saveOrderDetail(event.orderDetail)
    }
}
