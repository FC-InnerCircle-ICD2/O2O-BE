package org.fastcampus.applicationadmin.order.service.event

import org.fastcampus.order.repository.OrderDetailRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderDetailStatusEventHandler(
    private val orderDetailRepository: OrderDetailRepository,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(OrderDetailStatusEventHandler::class.java)
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleEvent(event: OrderDetailStatusEvent) {
        orderDetailRepository.updateStatus(event.orderId, mapOf("code" to event.status.code, "desc" to event.status.desc))
    }
}
