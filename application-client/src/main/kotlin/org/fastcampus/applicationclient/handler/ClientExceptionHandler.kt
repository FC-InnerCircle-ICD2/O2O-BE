package org.fastcampus.applicationclient.handler

import org.fastcampus.common.dto.APIResponseDTO
import org.fastcampus.order.exception.OrderException
import org.fastcampus.payment.exception.PaymentException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ClientExceptionHandler {
    companion object {
        private val logger = LoggerFactory.getLogger(ClientExceptionHandler::class.java)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleException(exception: Exception): ResponseEntity<APIResponseDTO<Void>> {
        logger.error("handleException: ", exception)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .body(APIResponseDTO(500, "관리자에게 문의 바랍니다.", null))
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
