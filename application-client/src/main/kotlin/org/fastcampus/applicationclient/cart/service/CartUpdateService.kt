package org.fastcampus.applicationclient.cart.service

import org.fastcampus.applicationclient.cart.controller.dto.request.CartUpdateRequest
import org.fastcampus.applicationclient.cart.controller.dto.response.CartUpdateResponse
import org.fastcampus.cart.exception.CartException
import org.fastcampus.cart.repository.CartRepository
import org.springframework.stereotype.Service

@Service
class CartUpdateService(
    private val cartRepository: CartRepository,
) {
    fun updateCart(userId: Long, cartUpdateRequest: CartUpdateRequest): CartUpdateResponse {
        // 현재 카트 찾기
        val cart = cartRepository.findByUserId(userId) ?: throw CartException.CartNotFound()

        // 수정대상 메뉴
        val cartMenu = cart.orderMenus.firstOrNull { it.sequence == cartUpdateRequest.cartId } ?: throw CartException.CartMenuNotFound()

        // 수량 변경
        cartMenu.quantity = cartUpdateRequest.quantity

        // 저장
        cartRepository.save(cart)

        return CartUpdateResponse(cartId = cartMenu.sequence, quantity = cartMenu.quantity)
    }
}
