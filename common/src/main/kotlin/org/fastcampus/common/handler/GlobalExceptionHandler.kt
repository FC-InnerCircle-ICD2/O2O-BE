package org.fastcampus.common.handler

import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Created by brinst07 on 25. 1. 9..
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException::class)
    fun handlerRuntime(exception: RuntimeException): ResponseEntity<APIResponseDTO<Void>>? {
        return null
    }
}
