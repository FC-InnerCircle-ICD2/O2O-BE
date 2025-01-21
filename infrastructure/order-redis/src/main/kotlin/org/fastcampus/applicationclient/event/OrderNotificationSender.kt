package org.fastcampus.applicationclient.event

import org.fastcampus.order.event.NotificationSender
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component
class OrderNotificationSender(
    private val stringRedisTemplate: StringRedisTemplate,
) : NotificationSender {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(OrderNotificationSender::class.java)

        private const val CHANNEL = "ORDER_NOTIFICATION"
    }

    override fun send(message: String) {
        logger.debug("send message: {}", message)
        stringRedisTemplate.convertAndSend(CHANNEL, message)
    }
}
