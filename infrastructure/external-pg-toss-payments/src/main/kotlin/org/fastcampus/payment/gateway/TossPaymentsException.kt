package org.fastcampus.payment.gateway

internal data class TossPaymentsException(
    override val message: String,
    val error: TossPaymentsErrorResponse? = null,
) : RuntimeException(message)
