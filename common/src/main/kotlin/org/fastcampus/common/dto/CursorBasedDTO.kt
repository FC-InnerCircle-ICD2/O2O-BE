package org.fastcampus.common.dto

data class CursorDTO<T>(
    val content: List<T>,
    val nextCursor: Int?,
)

data class CursorBasedDTO<T>(
    val content: List<T>,
    val isEnd: Boolean,
    val totalCount: Long,
)
