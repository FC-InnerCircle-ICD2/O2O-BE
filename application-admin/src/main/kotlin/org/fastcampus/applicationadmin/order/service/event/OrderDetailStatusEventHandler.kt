package org.fastcampus.applicationadmin.order.service.event

import org.fastcampus.order.repository.OrderDetailRepository
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class OrderDetailStatusEventHandler(
    private val orderDetailRepository: OrderDetailRepository,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(OrderDetailStatusEventHandler::class.java)
    }

    @Async
    @EventListener
    fun handleEvent(event: OrderDetailStatusEvent) {
        orderDetailRepository.updateStatus(event.orderId, mapOf("code" to event.status.code, "desc" to event.status.desc))
    }
}
