package org.fastcampus.applicationclient.order.controller

import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.config.security.dto.JwtAuthenticated
import org.fastcampus.applicationclient.order.controller.docs.OrderControllerDocs
import org.fastcampus.applicationclient.order.controller.dto.request.OrderCreationRequest
import org.fastcampus.applicationclient.order.controller.dto.response.OrderCreationResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderDetailResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderStatusResponse
import org.fastcampus.applicationclient.order.service.OrderCancellationService
import org.fastcampus.applicationclient.order.service.OrderCreationService
import org.fastcampus.applicationclient.order.service.OrderService
import org.fastcampus.common.dto.APIResponseDTO
import org.fastcampus.common.dto.CursorDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
    private val orderService: OrderService,
    private val orderCancellationService: OrderCancellationService,
    private val orderCreationService: OrderCreationService,
) : OrderControllerDocs {
    @JwtAuthenticated
    @GetMapping
    fun getOrders(
        @RequestParam keyword: String,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "5") size: Int,
        @AuthenticationPrincipal authMember: AuthMember,
    ): ResponseEntity<APIResponseDTO<CursorDTO<OrderResponse>>> {
        val response = orderService.getOrders(authMember.id, keyword, page - 1, size)
        return ResponseEntity
            .ok(APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, response))
    }

    @GetMapping("/{orderId}")
    fun getOrder(
        @PathVariable orderId: String,
    ): ResponseEntity<APIResponseDTO<OrderDetailResponse>> {
        val response = orderService.getOrder(orderId)
        return ResponseEntity
            .ok(APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, response))
    }

    @GetMapping("/{orderId}/status")
    fun getOrderStatus(
        @PathVariable orderId: String,
    ): ResponseEntity<APIResponseDTO<OrderStatusResponse>> {
        val response = orderService.getOrderStatus(orderId)
        return ResponseEntity
            .ok(APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, response))
    }

    @JwtAuthenticated
    @PostMapping
    override fun createOrder(
        @RequestHeader("X-User-Lat") userLat: Double,
        @RequestHeader("X-User-Lng") userLng: Double,
        @RequestBody orderCreationRequest: OrderCreationRequest,
        @AuthenticationPrincipal authMember: AuthMember,
    ): APIResponseDTO<OrderCreationResponse> {
        val response = orderCreationService.createOrder(authMember.id, orderCreationRequest, Pair(userLat, userLng))
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, response)
    }

    @JwtAuthenticated
    @PatchMapping("/{orderId}/cancel")
    fun cancelOrder(
        @PathVariable("orderId") orderId: String,
        @AuthenticationPrincipal authMember: AuthMember,
    ): APIResponseDTO<Nothing?> {
        orderCancellationService.cancelOrder(orderId, authMember.id)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, null)
    }
}
