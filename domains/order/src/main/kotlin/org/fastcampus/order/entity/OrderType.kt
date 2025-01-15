package org.fastcampus.order.entity

enum class OrderType(
    val code: String,
    val desc: String
) {
    DELIVERY("T1", "배달"),
    PACKING("T2", "포장")
}
