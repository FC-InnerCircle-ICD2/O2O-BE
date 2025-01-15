package org.fastcampus.order.entity

enum class OrderStatus(
    val code: String,
    val desc: String
) {
    ORDER_COMPLETE("S1", "주문완료"),
    ORDER_PROGRESS("S2", "주문 진행중"),
    DELIVERY_COMPLETED("S3", "배달 완료")
}
