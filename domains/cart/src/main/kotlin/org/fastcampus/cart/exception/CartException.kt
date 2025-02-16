package org.fastcampus.cart.exception

open class CartException(message: String) : RuntimeException(message) {
    data class StoreNotMatches(val storeId: String) : CartException("기존 등록된 가게와 일치하지 않습니다.")

    data class StoreNotFound(val storeId: String) : CartException("가게를 찾을 수 없습니다.")
}
