package org.fastcampus.order.document

import org.bson.types.ObjectId
import org.fastcampus.order.entity.Order
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document(collection = "orders")
data class OrderDocument(
    @Id
    @Field(name = "_id")
    val _id: ObjectId? = null,
    val orderId: String,
    val storeId: String?,
    val storeName: String?,
    val storeImageThumbnail: String?,
    val userId: Long?,
    val status: Order.Status,
    val orderTime: LocalDateTime,
    val orderSummary: String?,
    val isDeleted: Boolean,
    val tel: String?,
    val roadAddress: String?,
    val jibunAddress: String?,
    val detailAddress: String?,
    val excludingSpoonAndFork: Boolean,
    val requestToRider: String?,
    val orderPrice: Long,
    val deliveryPrice: Long?,
    val deliveryCompleteTime: LocalDateTime?,
    val paymentPrice: Long,
    val paymentId: Long,
    val paymentType: Map<String, String?>,
    val type: Order.Type,
    @Field(name = "orderMenus")
    val orderMenuDocument: List<OrderMenuDocument>? = null,
)

fun Order.toJpaDocument(orderId: String, paymentType: Map<String, String>) =
    OrderDocument(
        null,
        orderId,
        storeId,
        storeName,
        storeImageThumbnail,
        userId,
        status,
        orderTime,
        orderSummary,
        isDeleted,
        tel,
        roadAddress,
        jibunAddress,
        detailAddress,
        excludingSpoonAndFork,
        requestToRider,
        orderPrice,
        deliveryPrice,
        deliveryCompleteTime,
        paymentPrice,
        paymentId,
        mapOf("code" to paymentType["code"], "desc" to paymentType["desc"]),
        type,
    )

fun OrderDocument.toModel() =
    Order(
        orderId,
        storeId,
        storeName,
        storeImageThumbnail,
        userId,
        roadAddress,
        jibunAddress,
        detailAddress,
        tel,
        status,
        orderTime,
        orderSummary,
        type,
        paymentId,
        isDeleted,
        deliveryCompleteTime,
        orderPrice,
        deliveryPrice,
        paymentPrice,
        excludingSpoonAndFork,
        requestToRider,
        orderMenuDocument?.map { it.toModel() },
    )
