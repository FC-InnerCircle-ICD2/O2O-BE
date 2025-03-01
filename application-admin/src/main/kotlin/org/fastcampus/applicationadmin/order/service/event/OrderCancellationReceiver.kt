package org.fastcampus.applicationadmin.order.service.event

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.fastcampus.applicationadmin.sse.SseManager
import org.fastcampus.order.event.NotificationReceiver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component("OrderCancellationReceiver")
class OrderCancellationReceiver(
    private val sseManager: SseManager,
    private val objectMapper: ObjectMapper,
) : NotificationReceiver {
    override fun handleMessage(message: String) {
        logger.debug("Received message {}", message)

        val readValue = objectMapper.readValue(message, object : TypeReference<Map<String, String>>() {}) ?: emptyMap()
        val ownerId = readValue["ownerId"] ?: "ownerId 없음"
        val orderId = readValue["orderId"] ?: "orderId 없음"

        sseManager.push(
            key = ownerId,
            eventType = "ORDER_CANCELLATION",
            data = objectMapper.writeValueAsString(mapOf("orderId" to orderId)),
        )
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(OrderCancellationReceiver::class.java)
    }
}
