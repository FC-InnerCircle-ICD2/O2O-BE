package org.fastcampus.payment.gateway.error

internal data class TossPaymentsException(
    override val message: String,
    val error: TossPaymentsApproveErrorResponse? = null,
) : RuntimeException(message)
