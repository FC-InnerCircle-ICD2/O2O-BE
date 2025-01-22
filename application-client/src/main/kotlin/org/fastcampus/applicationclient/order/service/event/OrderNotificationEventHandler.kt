package org.fastcampus.applicationclient.order.service.event

import com.fasterxml.jackson.databind.ObjectMapper
import org.fastcampus.order.event.NotificationSender
import org.fastcampus.order.event.OrderNotificationEvent
import org.fastcampus.order.event.OrderNotificationMessage
import org.fastcampus.order.repository.OrderMenuOptionGroupRepository
import org.fastcampus.order.repository.OrderMenuOptionRepository
import org.fastcampus.order.repository.OrderMenuRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderNotificationEventHandler(
    private val orderMenuRepository: OrderMenuRepository,
    private val orderMenuOptionGroupRepository: OrderMenuOptionGroupRepository,
    private val orderMenuOptionRepository: OrderMenuOptionRepository,
    private val objectMapper: ObjectMapper,
    private val orderNotificationSender: NotificationSender,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(OrderNotificationEventHandler::class.java)
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleEvent(event: OrderNotificationEvent) {
        logger.debug("주문알림 이벤트 처리: {}", event.order)

        val stringMessage = objectMapper.writeValueAsString(
            OrderNotificationMessage.fromOrder(
                event.order,
                orderMenuRepository,
                orderMenuOptionGroupRepository,
                orderMenuOptionRepository,
            ),
        )

        orderNotificationSender.send(stringMessage)
    }
}
