package org.fastcampus.payment.exception

class PaymentException(
    val status: Int,
    override val message: String,
) : RuntimeException(message)
