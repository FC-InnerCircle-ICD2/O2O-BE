package org.fastcampus.payment.exception

open class PaymentException(message: String) : RuntimeException(message) {
    data class PaymentNotFound(val orderId: String) : PaymentException("결제 정보를 찾을 수 없습니다.")

    data class UserNotMatching(val msg: String) : PaymentException("주문 유저가 일치하지 않습니다.")

    data class IncorrectAmount(val msg: String) : PaymentException("주문 금액이 올바르지 않습니다.")

    data class OrderNotFound(val orderId: String) : PaymentException("주문 정보를 찾을 수 없습니다.")
}
