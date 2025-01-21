package org.fastcampus.store.mongo.document

import org.bson.types.ObjectId
import org.fastcampus.store.entity.Store
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

/**
 * Created by brinst07 on 25. 1. 11..
 */

@Document(collection = "stores")
class StoreDocument(
    @Id
    @Field(name = "_id")
    val _id: ObjectId? = null,
    val id: String?,
    val name: String?,
    val address: String?,
    @Field("location")
    val location: GeoJsonPoint,
    val border: String?,
    val ownerId: String?,
    val tel: String?,
    val imageThumbnail: String?,
    val imageMain: String?,
    val status: Store.Status,
    val breakTime: String,
    val roadAddress: String?,
    val jibunAddress: String?,
    val category: Store.Category?,
    @Field(name = "storeMenuCategory")
    val storeMenuCategoryDocument: List<StoreMenuCategoryDocument>?,
)

fun StoreDocument.toModel() =
    Store(
        _id.toString(),
        id,
        name,
        address,
        location.y,
        location.x,
        border?.toInt(),
        ownerId,
        tel,
        imageThumbnail,
        imageMain,
        status,
        breakTime,
        roadAddress,
        jibunAddress,
        category,
        storeMenuCategoryDocument?.map { it.toModel() },
    )
