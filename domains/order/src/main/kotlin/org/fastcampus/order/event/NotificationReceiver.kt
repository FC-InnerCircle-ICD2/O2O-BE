package org.fastcampus.order.event

interface NotificationReceiver {
    fun handleMessage(message: String)
}
