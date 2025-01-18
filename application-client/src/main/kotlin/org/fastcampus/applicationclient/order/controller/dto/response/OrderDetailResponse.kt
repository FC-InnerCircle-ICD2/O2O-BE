package org.fastcampus.applicationclient.order.controller.dto.response

data class OrderDetailResponse(
    val orderId: String,
    val isDeleted: Boolean,
    val tel: String?,
    val roadAddress: String?,
    val jibunAddress: String?,
    val detailAddress: String?,
    val orderPrice: Long,
    val deliveryPrice: Long?,
    val paymentPrice: Long,
    val paymentId: Long,
    val paymentType: Map<String, String>,
    val type: Map<String, String>,
    val orderMenus: List<OrderMenuResponse>,
)
