package org.fastcampus.payment.entity

data class Payment(
    val id: Long? = null,
    val type: Type,
    val paymentPrice: Long?,
    val status: Status = Status.WAIT,
    val pgKey: String? = null,
) {
    enum class Type(
        val code: String,
        val desc: String,
    ) {
        PAY_200("P1", "PAY200"),
        TOSS_PAY("P2", "토스페이"),
    }

    enum class Status(
        val code: String,
        val desc: String,
    ) {
        WAIT("S1", "결제대기"),
        COMPLETED("S2", "결제완료"),
        FAILED("S3", "결제실패"),
    }
}
