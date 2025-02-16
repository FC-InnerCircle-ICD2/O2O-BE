package org.fastcampus.cart.exception

open class CartException(message: String) : RuntimeException(message) {
    data class StoreNotMatches(val storeId: String) : CartException("기존 등록된 가게와 일치하지 않습니다.")

    data class StoreNotFound(val storeId: String) : CartException("가게를 찾을 수 없습니다.")

    class CartNotFound : CartException("사용자의 장바구니를 찾을 수 없습니다.")

    class CartMenuNotFound : CartException("대상 장바구니 메뉴를 찾을 수 없습니다.")
}
