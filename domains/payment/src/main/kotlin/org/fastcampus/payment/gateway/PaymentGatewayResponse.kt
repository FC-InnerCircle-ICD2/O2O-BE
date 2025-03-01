package org.fastcampus.payment.gateway

data class PaymentGatewayResponse(
    val status: Status,
    val message: String = "",
    val paymentKey: String = "",
    val orderId: String = "",
    val amount: Long = 0,
) {
    enum class Status {
        DONE, // 결제 승인된 상태
        ABORTED, // 결제 승인 실패 상태
        EXPIRED, // 결제 유효 시간이 지나 거래가 취소된 상태
        CANCELED, // 승인된 결제가 취소된 상태
        FAILED, // toss payments 응답 에러코드일 때
    }
}
