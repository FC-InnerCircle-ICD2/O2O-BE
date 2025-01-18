package org.fastcampus.common.dto

data class CursorBasedDTO<T>(
    val content: List<T>,
    val isEnd: Boolean,
    val totalCount: Long,
)
