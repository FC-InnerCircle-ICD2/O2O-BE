package org.fastcampus.applicationadmin.order.service.event

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.fastcampus.applicationadmin.sse.SseManager
import org.fastcampus.order.entity.OrderReceivedEvent
import org.fastcampus.order.event.NotificationReceiver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component("OrderNotificationReceiver")
class OrderNotificationReceiver(
    private val sseManager: SseManager,
    private val objectMapper: ObjectMapper,
) : NotificationReceiver {
    override fun handleMessage(message: String) {
        logger.debug("Received message {}", message)

        objectMapper.readValue(message, object : TypeReference<Map<String, OrderReceivedEvent>>() {})
            .forEach { (storeId, orderReceivedEvent) ->
                logger.debug("storeId: [{}]", storeId)
                sseManager.push(
                    key = storeId,
                    eventType = "ORDER_NOTIFICATION",
                    data = objectMapper.writeValueAsString(orderReceivedEvent),
                )
            }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(OrderNotificationReceiver::class.java)
    }
}
