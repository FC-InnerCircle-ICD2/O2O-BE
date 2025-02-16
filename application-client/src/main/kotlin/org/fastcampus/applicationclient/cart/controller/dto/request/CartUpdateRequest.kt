package org.fastcampus.applicationclient.cart.controller.dto.request

data class CartUpdateRequest(
    val cartId: Long,
    val quantity: Long,
)
