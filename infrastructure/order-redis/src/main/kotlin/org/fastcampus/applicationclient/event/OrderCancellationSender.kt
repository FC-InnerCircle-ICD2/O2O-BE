package org.fastcampus.applicationclient.event

import org.fastcampus.order.event.NotificationSender
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component("OrderCancellationSender")
class OrderCancellationSender(
    private val stringRedisTemplate: StringRedisTemplate,
) : NotificationSender {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(OrderCancellationSender::class.java)

        private const val CHANNEL = "ORDER_CANCELLATION"
    }

    override fun send(message: String) {
        logger.debug("send message: {}", message)
        stringRedisTemplate.convertAndSend(CHANNEL, message)
    }
}
