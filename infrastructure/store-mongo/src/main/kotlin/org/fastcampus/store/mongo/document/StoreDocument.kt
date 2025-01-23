package org.fastcampus.store.mongo.document

import com.mongodb.client.model.geojson.Point
import org.bson.types.ObjectId
import org.fastcampus.store.entity.Store
import org.springframework.data.annotation.Id
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
    val address: String?,
    val border: String?,
    val breakTime: String?,
    val category: Store.Category?,
    val id: String?,
    val status: Store.Status,
    val imageMain: String?,
    val ownerId: String?,
    val name: String?,
    @Field(name = "location")
    val location: Point, // location 객체
    val tel: String?,
    val imageThumbnail: String?,
    val roadAddress: String?,
    val jibunAddress: String?,
    @Field(name = "storeMenuCategory")
    val storeMenuCategoryDocument: List<StoreMenuCategoryDocument>? = null,
)

fun StoreDocument.toModel() =
    Store(
        _id.toString(),
        address,
        border ?: "unknown",
        breakTime,
        category,
        id,
        name,
        location.coordinates.values[1] ?: 0.0, // location.coordinates[1]
        jibunAddress,
        location.coordinates.values[0] ?: 0.0, // location.coordinates[0]
        ownerId,
        tel,
        imageThumbnail,
        imageMain,
        status,
        roadAddress,
        storeMenuCategoryDocument?.map { it.toModel() },
    )
