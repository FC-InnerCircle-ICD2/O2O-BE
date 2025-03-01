package org.fastcampus.payment.exception

open class PaymentException(message: String) : RuntimeException(message) {
    data class PaymentNotFound(val orderId: String) : PaymentException("결제 정보를 찾을 수 없습니다.")

    data class UserNotMatching(val msg: String) : PaymentException("주문 유저가 일치하지 않습니다.")

    data class OrderStatusIsNotWaiting(val orderId: String) : PaymentException("주문 상태가 올바르지 않습니다.")

    data class IncorrectAmount(val msg: String) : PaymentException("주문 금액이 올바르지 않습니다.")

    data class OrderNotFound(val orderId: String) : PaymentException("주문 정보를 찾을 수 없습니다.")

    data class PgKeyNotExists(val paymentId: String) : PaymentException("PG Key가 존재하지 않습니다.")

    data class NotSupportedPaymentType(val paymentType: String) : PaymentException("지원하지 않는 결제 타입입니다.")

    // PG 요청에 대한 실패 공통 - PG 클라이언트 모듈이 반환하는 메세지를 그대로 클라이언트측까지 사용
    data class PGFailed(val paymentId: String, val msg: String) : PaymentException(msg)
}
