package org.fastcampus.applicationclient.handler

import org.fastcampus.common.dto.APIResponseDTO
import org.fastcampus.order.exception.OrderException
import org.fastcampus.payment.exception.PaymentException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ClientExceptionHandler {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ClientExceptionHandler::class.java)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handlerRuntime(exception: RuntimeException): ResponseEntity<APIResponseDTO<Void>>? {
        logger.error(exception.message)
        return ResponseEntity.status(
            HttpStatus.INTERNAL_SERVER_ERROR,
        ).body(APIResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.message, null))
    }

    /**
     * Validation 실패 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(exception: MethodArgumentNotValidException): ResponseEntity<APIResponseDTO<Map<String, String>>> {
        logger.error("Validation Exception: ${exception.message}", exception)

        // 필드 에러 메시지 맵핑
        val errors = exception.bindingResult.allErrors.associate { error ->
            val fieldName = if (error is FieldError) error.field else "unknown"
            fieldName to (error.defaultMessage ?: "Invalid value")
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                APIResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    "Validation failed",
                    errors,
                ),
            )
    }

    @ExceptionHandler(OrderException::class)
    fun handleOrderException(exception: OrderException): ResponseEntity<APIResponseDTO<*>> {
        logger.error("handleOrderException: ", exception)
        return ResponseEntity
            .status(exception.status)
            .body(APIResponseDTO(exception.status, "FAIL", exception.message))
    }

    @ExceptionHandler(PaymentException::class)
    fun handleOrderException(exception: PaymentException): ResponseEntity<APIResponseDTO<*>> {
        logger.error("handleOrderException: ", exception)
        return ResponseEntity
            .status(exception.status)
            .body(APIResponseDTO(exception.status, "FAIL", exception.message))
    }
}
