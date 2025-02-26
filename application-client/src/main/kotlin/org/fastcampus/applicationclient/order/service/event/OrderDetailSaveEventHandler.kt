package org.fastcampus.applicationclient.order.service.event

import org.fastcampus.order.repository.OrderDetailRepository
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class OrderDetailSaveEventHandler(
    private val orderDetailRepository: OrderDetailRepository,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(OrderDetailSaveEventHandler::class.java)
    }

    @EventListener
    fun handleEvent(event: OrderDetailSaveEvent) {

    }
}
