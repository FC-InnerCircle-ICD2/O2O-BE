package org.fastcampus.applicationclient.cart.controller

import org.fastcampus.applicationclient.cart.controller.dto.request.CartMenuInsertionRequest
import org.fastcampus.applicationclient.cart.controller.dto.request.CartUpdateRequest
import org.fastcampus.applicationclient.cart.controller.dto.response.CartResponse
import org.fastcampus.applicationclient.cart.controller.dto.response.CartUpdateResponse
import org.fastcampus.applicationclient.cart.service.CartMenuInsertionService
import org.fastcampus.applicationclient.cart.service.CartReadService
import org.fastcampus.applicationclient.cart.service.CartUpdateService
import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.config.security.dto.JwtAuthenticated
import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/carts")
class CartController(
    private val cartMenuInsertionService: CartMenuInsertionService,
    private val cartReadService: CartReadService,
    private val cartUpdateService: CartUpdateService,
) {
    @JwtAuthenticated
    @PostMapping
    fun insertCartMenu(
        @AuthenticationPrincipal authMember: AuthMember,
        @RequestBody request: CartMenuInsertionRequest,
    ): APIResponseDTO<Void> {
        cartMenuInsertionService.insertCartMenu(authMember.id, request)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, null)
    }

    @JwtAuthenticated
    @GetMapping
    fun getCart(
        @AuthenticationPrincipal authMember: AuthMember,
    ): APIResponseDTO<CartResponse> {
        val cartResponse = cartReadService.getCart(authMember.id)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, cartResponse)
    }

    @JwtAuthenticated
    @PatchMapping
    fun updateCart(
        @AuthenticationPrincipal authMember: AuthMember,
        @RequestBody request: CartUpdateRequest,
    ): APIResponseDTO<CartUpdateResponse> {
        val response = cartUpdateService.updateCart(authMember.id, request)
        return APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, response)
    }
}
