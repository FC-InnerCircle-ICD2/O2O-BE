package org.fastcampus.payment.entity

data class Refund(
    val id: Long? = null,
    val status: Status,
    val orderId: String,
) {
    fun fail(): Refund {
        if (this.status == Status.COMPLETE) {
            throw IllegalStateException("이미 환불 완료된 거래입니다.")
        }
        return Refund(id, Status.FAIL, orderId)
    }

    fun complete(): Refund {
        return Refund(id, Status.COMPLETE, orderId)
    }

    enum class Status(
        val code: String,
        val decs: String,
    ) {
        WAIT("f1", "환불대기"),
        COMPLETE("f2", "환불완료"),
        FAIL("f3", "환불실패"),
    }
}
