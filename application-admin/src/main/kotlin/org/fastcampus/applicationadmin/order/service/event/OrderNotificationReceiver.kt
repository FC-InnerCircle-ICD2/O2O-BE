package org.fastcampus.applicationadmin.order.service.event

import org.fastcampus.order.event.NotificationReceiver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component("OrderNotificationReceiver")
class OrderNotificationReceiver : NotificationReceiver {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(OrderNotificationReceiver::class.java)
    }

    override fun handleMessage(message: String) {
        logger.debug("Received message {}", message)
    }
}
