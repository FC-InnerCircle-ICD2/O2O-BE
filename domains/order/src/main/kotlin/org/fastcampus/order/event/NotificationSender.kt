package org.fastcampus.order.event

interface NotificationSender {
    fun send(message: String)
}
