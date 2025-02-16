package org.fastcampus.applicationclient.cart.controller.dto.request

data class CartMenuDeletionRequest(
    val cartIds: List<Long> = emptyList(),
)
