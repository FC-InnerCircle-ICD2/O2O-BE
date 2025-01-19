package org.fastcampus.applicationclient.handler

import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ClientExceptionHandler {
    @ExceptionHandler(RuntimeException::class)
    fun handlerRuntime(exception: RuntimeException): ResponseEntity<APIResponseDTO<Void>>? {
        return null
    }
}
