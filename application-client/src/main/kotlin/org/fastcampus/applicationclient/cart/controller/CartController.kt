package org.fastcampus.applicationclient.cart.controller

import org.fastcampus.applicationclient.cart.controller.dto.request.CartMenuInsertionRequest
import org.fastcampus.applicationclient.cart.service.CartMenuInsertionService
import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.config.security.dto.JwtAuthenticated
import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/cart")
class CartController(
    private val cartMenuInsertionService: CartMenuInsertionService,
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
}
