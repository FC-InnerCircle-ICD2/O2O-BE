package org.fastcampus.payment.gateway.error

import org.fastcampus.payment.gateway.client.TossPaymentsApproveErrorResponse

internal data class TossPaymentsException(
    override val message: String,
    val error: TossPaymentsApproveErrorResponse? = null,
) : RuntimeException(message)
