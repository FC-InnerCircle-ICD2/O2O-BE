package org.fastcampus.store.mongo.document

import org.fastcampus.store.entity.Store
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by brinst07 on 25. 1. 11..
 */

@Document(collection = "STORE")
class StoreDocument(
    @Id
    val id: String? = null,
    val name: String?,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val border: Int?,
    val ownerId: String?,
    val tel: String?,
    val imageThumbnail: String?,
    val imageMain: String?,
    val status: Store.Status,
    val breakTime: String,
    val roadAddress: String?,
    val jibunAddress: String?,
    val category: Store.Category?,
    val storeMenuCategoryDocument: List<StoreMenuCategoryDocument>?,
)

fun StoreDocument.toModel() =
    Store(
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
        category,
        storeMenuCategoryDocument?.map { it.toModel() }
    )
