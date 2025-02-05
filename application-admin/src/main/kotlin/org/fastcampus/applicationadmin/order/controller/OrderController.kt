package org.fastcampus.applicationadmin.order.controller

import org.fastcampus.applicationadmin.config.security.dto.AuthMember
import org.fastcampus.applicationadmin.order.controller.dto.OrderInquiryResponse
import org.fastcampus.applicationadmin.order.service.OrderService
import org.fastcampus.common.dto.APIResponseDTO
import org.fastcampus.common.dto.OffSetBasedDTO
import org.fastcampus.order.entity.Order
import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
    private val orderService: OrderService,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(OrderController::class.java)
    }

    @GetMapping
    fun getOrders(
        @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") startDate: LocalDate,
        @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") endDate: LocalDate,
        @RequestParam(required = true) status: List<Order.ClientStatus>,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int,
        @AuthenticationPrincipal authMember: AuthMember,
    ): ResponseEntity<APIResponseDTO<OffSetBasedDTO<OrderInquiryResponse>>> {
        logger.info(
            "parameters in order inquiry: userId: ${authMember.id}, startDate: $startDate, endDate: $endDate, status: $status",
        )
        val response = orderService.getOrdersByStatusesWithPeriod(
            authMember.id,
            status,
            startDate,
            endDate,
            page,
            size,
        )
        return ResponseEntity
            .ok(APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, response))
    }

    @PatchMapping("/{orderId}/accept")
    fun acceptOrder(
        @PathVariable("orderId") orderId: String,
    ): ResponseEntity<APIResponseDTO<Nothing?>> {
        orderService.acceptOrder(orderId)
        return ResponseEntity
            .ok(APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, null))
    }

    @PatchMapping("/{orderId}/refuse")
    fun refuseOrder(
        @PathVariable orderId: String,
    ): ResponseEntity<APIResponseDTO<Unit>> {
        return ResponseEntity.ok(APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, orderService.refuseOrder(orderId)))
    }
}
