package org.fastcampus.common.enum

/**
 * Created by brinst07 on 25. 1. 6..
 */
enum class ErrorCode(
    val status: Int,
    val message: String,
) {
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
}
