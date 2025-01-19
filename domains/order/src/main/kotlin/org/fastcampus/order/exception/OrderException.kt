package org.fastcampus.order.exception

class OrderException(
    val status: Int,
    override val message: String,
) : RuntimeException(message)
