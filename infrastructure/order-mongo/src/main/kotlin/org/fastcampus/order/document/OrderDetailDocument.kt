package org.fastcampus.order.document

import org.bson.types.ObjectId
import org.fastcampus.order.entity.OrderDetail
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document(collection = "orderDetails")
class OrderDetailDocument(
    @Id
    @Field(name = "_id")
    val _id: ObjectId? = null,
    val orderId: String,
    val storeId: String?,
    val storeName: String?,
    val storeImageThumbnail: String?,
    val userId: Long?,
    val status: Map<String, String?>,
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
    val type: Map<String, String?>,
    @Field(name = "orderMenus")
    val orderMenuDocument: List<OrderMenuDocument>? = null,
)

fun OrderDetail.toJpaDocument() =
    OrderDetailDocument(
        null,
        orderId,
        storeId,
        storeName,
        storeImageThumbnail,
        userId,
        mapOf("code" to status["code"], "desc" to status["desc"]),
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
        mapOf("code" to type["code"], "desc" to type["desc"]),
        orderMenus?.map { it.toJpaDocument() },
    )

fun OrderDetailDocument.toModel() =
    OrderDetail(
        orderId,
        storeId,
        storeName,
        storeImageThumbnail,
        userId,
        roadAddress,
        jibunAddress,
        detailAddress,
        tel,
        mapOf("code" to status["code"], "desc" to status["desc"]),
        orderTime,
        orderSummary,
        mapOf("code" to type["code"], "desc" to type["desc"]),
        paymentId,
        mapOf("code" to paymentType["code"], "desc" to paymentType["desc"]),
        isDeleted,
        deliveryCompleteTime,
        orderPrice,
        deliveryPrice,
        paymentPrice,
        excludingSpoonAndFork,
        requestToRider,
        orderMenuDocument?.map { it.toModel() },
    )
