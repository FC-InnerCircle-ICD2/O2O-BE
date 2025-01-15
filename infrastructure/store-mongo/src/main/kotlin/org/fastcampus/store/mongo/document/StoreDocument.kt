package org.fastcampus.store.mongo.document

import org.fastcampus.store.entity.Store
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by brinst07 on 25. 1. 11..
 */

@Document(collection = "TB_STORE")
class StoreDocument(
    @Id
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


fun StoreDocument.toModel() = Store(
    id,
    name,
    address,
    latitude,
    longitude,
    border,
    ownerId,
    tel,
    imageThumbnail,
    imageMain,
    status,
    breakTime,
    roadAddress,
    jibunAddress,
    category
)
