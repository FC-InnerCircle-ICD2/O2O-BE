package org.fastcampus.order.entity

import java.time.LocalDateTime

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class Order(
    val id: String,
    val storeId: String,
    val userId: Long,
    val roadAddress: String?,
    val jibunAddress: String?,
    val detailAddress: String?,
    val tel: String?,
    val status: Status,
    val orderTime: LocalDateTime,
    val type: Type,
    val paymentId: Long,
    val isDeleted: Boolean,
    val deliveryCompleteTime: LocalDateTime?,
    val orderPrice: Long?,
    val deliveryPrice: Long?,
    val paymentPrice: Long?,
) {
    enum class Status(
        val code: String,
        val desc: String,
    ) {
        WAIT("S1", "주문대기"),
        RECEIVE("S2", "주문접수"),
        ACCEPT("S3", "주문수락"),
        REFUSE("S4", "주문거절"),
        COMPLETED("S5", "주문완료"),
        CANCEL("S6", "주문취소"),
    }

    enum class Type(
        val code: String,
        val desc: String,
    ) {
        DELIVERY("T1", "배달"),
        PACKING("T2", "포장"),
    }
}
