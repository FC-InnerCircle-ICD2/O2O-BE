package org.fastcampus.applicationclient.handler

import org.fastcampus.common.dto.APIResponseDTO
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Created by kms0902 on 25. 1. 19..
 */
@RestControllerAdvice
class CeoExceptionHandler {
    companion object {
        private val logger = LoggerFactory.getLogger(CeoExceptionHandler::class.java)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<APIResponseDTO<Map<String, String>>> {
        val errors = ex.bindingResult.fieldErrors.associate { error ->
            error.field to (error.defaultMessage ?: "Invalid value")
        }

        logger.error("Validation failed: $errors")
        return ResponseEntity
            .badRequest()
            .body(
                APIResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.reasonPhrase,
                    errors,
                ),
            )
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntime(ex: RuntimeException): ResponseEntity<APIResponseDTO<Void>> {
        logger.error("Unexpected error occurred", ex)
        return ResponseEntity
            .internalServerError()
            .body(
                APIResponseDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ex.message ?: "Internal server error",
                    null,
                ),
            )
    }
}
