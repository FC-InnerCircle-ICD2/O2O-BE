package org.fastcampus.applicationclient.handler

import org.fastcampus.common.dto.APIResponseDTO
import org.fastcampus.order.exception.OrderException
import org.fastcampus.payment.exception.PaymentException
import org.fastcampus.store.exception.StoreException
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
class ClientExceptionHandler {
    companion object {
        private val logger = LoggerFactory.getLogger(ClientExceptionHandler::class.java)
    }

    @ExceptionHandler(StoreException::class)
    fun handleStoreException(ex: StoreException): ResponseEntity<APIResponseDTO<Void>> {
        val (status, message) = when (ex) {
            is StoreException.StoreNotFoundException ->
                HttpStatus.NOT_FOUND to ex.message
            is StoreException.StoreCoordinatesNotFoundException ->
                HttpStatus.BAD_REQUEST to ex.message
            is StoreException.DeliveryCalculationException ->
                HttpStatus.INTERNAL_SERVER_ERROR to ex.message
        }

        logger.error("Store exception occurred: ${ex.message}")
        return ResponseEntity
            .status(status)
            .body(APIResponseDTO(status.value(), message, null))
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
                    "Validation failed",
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
