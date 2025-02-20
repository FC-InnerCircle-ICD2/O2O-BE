package org.fastcampus.common.dto

import java.time.LocalDateTime

data class CursorDTO<T>(
    val content: List<T>,
    val nextCursor: Int?,
    val totalCount: Long? = null, // 필요시 사용
)

data class CursorBasedDTO<T>(
    val content: List<T>,
    val isEnd: Boolean,
    val totalCount: Long,
)

data class TimeBasedCursorDTO<T>(
    val content: List<T>,
    val nextCursor: LocalDateTime?,
)
