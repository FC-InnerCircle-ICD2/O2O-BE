package org.fastcampus.applicationclient.favorite.controller.dto.response

data class FavoriteResponse(
    val id: String,
    val name: String,
    val imageMain: String,
    val rating: Double,
    val reviewCount: Long,
    val minimumOrderAmount: Long,
)
