package org.fastcampus.payment.gateway.error

internal data class Pay200Exception(
    override val message: String,
    val error: Pay200ApproveErrorResponse? = null,
) : RuntimeException(message)
