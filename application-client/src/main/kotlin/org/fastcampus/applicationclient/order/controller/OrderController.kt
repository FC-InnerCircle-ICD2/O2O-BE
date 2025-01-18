package org.fastcampus.applicationclient.order.controller

import org.fastcampus.applicationclient.order.controller.dto.response.OrderResponse
import org.fastcampus.applicationclient.order.service.OrderService
import org.fastcampus.common.dto.APIResponseDTO
import org.fastcampus.common.dto.CursorBasedDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
    private val orderService: OrderService,
) {
    @GetMapping
    fun getOrders(
        @RequestParam keyword: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int,
    ): ResponseEntity<APIResponseDTO<CursorBasedDTO<OrderResponse>>> {
        val response = orderService.getOrders(1, keyword, page, size)
        return ResponseEntity
            .ok(APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, response))
    }
}
