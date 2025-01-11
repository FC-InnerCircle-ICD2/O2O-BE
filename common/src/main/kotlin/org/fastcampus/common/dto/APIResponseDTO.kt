package org.fastcampus.common.dto

/**
 * Created by brinst07 on 25. 1. 6..
 */
data class APIResponseDTO<T>(
    val status: Int,
    val message: String?,
    val data: T?
)
