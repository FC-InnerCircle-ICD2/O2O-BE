package org.fastcampus.store.entity

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class Store(
    val id: Long? = null,
    val name: String?,
    val address: String?,
    val latitude: String?,
    val longitude: String?,
    val border: String?,
    val ownerId: String?,
    val tel: String?,
    val imageThumbnail: String?,
    val imageMain: String?,
    val status: String?,
    val breakTime: String?,
    val roadAddress: String?,
    val jibunAddress: String?,
    val category: String?,
)


