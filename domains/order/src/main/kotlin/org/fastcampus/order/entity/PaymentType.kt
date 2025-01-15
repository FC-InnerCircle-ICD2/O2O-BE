package org.fastcampus.order.entity

enum class PaymentType(
    val code: String,
    val desc: String,
) {
    KAKAO_PAY("P1", "카카오페이"),
    TOSS_PAY("P2", "토스페이")
}
