package org.fastcampus.applicationclient.store.utils

import org.fastcampus.common.dto.CursorDTO

object PaginationUtils {
    fun <T> List<T>.paginate(cursor: Int, size: Int): CursorDTO<T> {
        val adjustedCursor = (cursor - 1).coerceAtLeast(0)
        val startIndex = adjustedCursor * size
        val endIndex = (startIndex + size).coerceAtMost(this.size)

        val content = this.subList(startIndex, endIndex)
        val nextCursor = if (endIndex < this.size) cursor + 1 else null

        return CursorDTO(content, nextCursor)
    }
}
