package org.fastcampus.applicationclient.banner.controller.dto.response

data class BannerResponse(
    val id: Long,
    val imageUrl: String,
    val link: String,
    val title: String,
)
