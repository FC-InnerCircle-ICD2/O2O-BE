package org.fastcampus.order.entity

import java.time.LocalDateTime

data class OrderDetail(
    val orderId: String,
    val storeId: String?,
    val storeName: String?,
    val storeImageThumbnail: String?,
    val userId: Long?,
    val roadAddress: String?,
    val jibunAddress: String?,
    val detailAddress: String?,
    val tel: String?,
    val status: Map<String, String?>,
    val orderTime: LocalDateTime,
    val orderSummary: String?,
    val type: Map<String, String?>,
    val paymentId: Long,
    val paymentType: Map<String, String?>,
    val isDeleted: Boolean,
    val deliveryCompleteTime: LocalDateTime?,
    val orderPrice: Long,
    val deliveryPrice: Long?,
    val paymentPrice: Long,
    val excludingSpoonAndFork: Boolean = true,
    val requestToRider: String? = null,
    val orderMenus: List<OrderMenu>? = null,
)
