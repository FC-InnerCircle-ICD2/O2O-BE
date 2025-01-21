package org.fastcampus.applicationclient.order.service.event

import com.fasterxml.jackson.databind.ObjectMapper
import org.fastcampus.order.event.NotificationSender
import org.fastcampus.order.event.OrderNotification
import org.fastcampus.order.repository.OrderRepository
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class OrderNotificationHandler(
    private val orderRepository: OrderRepository,
    private val orderNotificationSender: NotificationSender,
    private val objectMapper: ObjectMapper,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(OrderNotificationHandler::class.java)
    }

    @Async
    @EventListener(OrderNotification::class)
    fun handleEvent(event: OrderNotification) {
        logger.debug("주문알림 이벤트 처리: {}", event.orderId)
        // TODO 정보 조회해서 메세지 전송
    }

}
