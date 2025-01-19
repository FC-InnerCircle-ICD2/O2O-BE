package org.fastcampus.payment.entity

data class Payment(
    val id: Long? = null,
    val type: Type,
    val paymentPrice: Long?,
) {
    enum class Type(
        val code: String,
        val desc: String,
    ) {
        KAKAO_PAY("P1", "카카오페이"),
        TOSS_PAY("P2", "토스페이"),
    }
}
