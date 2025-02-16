package org.fastcampus.applicationclient.cart.service

import org.fastcampus.cart.exception.CartException
import org.fastcampus.cart.repository.CartRepository
import org.springframework.stereotype.Service

@Service
class CartMenuDeletionService(
    private val cartRepository: CartRepository,
) {
    fun deleteCartMenu(userId: Long, cartIds: List<Long>) {
        // 현재 카트
        val cart = cartRepository.findByUserId(userId) ?: throw CartException.CartNotFound()

        // 제거요청 ID 제거
        val cartIdSet = cartIds.toSet()
        val cartMenus = cart.orderMenus.filterNot { cartIdSet.contains(it.sequence) }

        // 메뉴가 모두 사라졌다면 지운다.
        if (cartMenus.isEmpty()) {
            cartRepository.removeByUserId(userId)
            return
        }

        // 남아있다면 업데이트
        cartRepository.save(cart.copy(orderMenus = cartMenus))
    }
}
