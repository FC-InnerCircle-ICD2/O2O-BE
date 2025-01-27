package org.fastcampus.payment.exception

open class RefundException(message: String) : RuntimeException(message) {
    data class RefundNotFound(val id: Long) : RefundException("결제 정보를 찾을 수 없습니다.")
}
