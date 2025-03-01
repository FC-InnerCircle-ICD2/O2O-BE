package org.fastcampus.payment.gateway.client

import org.fastcampus.payment.gateway.PaymentGatewayResponse

internal data class TossPaymentsApproveErrorResponse(
    val code: String,
    val message: String,
) {
    fun toPaymentGatewayResponse(): PaymentGatewayResponse {
        return PaymentGatewayResponse(
            status = PaymentGatewayResponse.Status.FAILED,
            message = MESSAGE_BY_ERROR_CODE.getOrDefault(this.code, "결제 승인 처리에 실패하였습니다."),
        )
    }

    /**
     * 토스측 결제승인 에러코드 일부분. https://docs.tosspayments.com/reference/error-codes#코어-api-별-에러
     * 아래 설정한 에러코드 메세지 외 나머지는 통일된 메세지로 처리한다.
     */
    companion object {
        private val MESSAGE_BY_ERROR_CODE = mapOf(
            "ALREADY_PROCESSED_PAYMENT" to "이미 처리된 결제입니다.",
            "PROVIDER_ERROR" to "일시적인 오류가 발생하였습니다.",
            "INVALID_REJECT_CARD" to "카드 사용이 거절되었습니다.",
            "BELOW_MINIMUM_AMOUNT" to "신용카드는 결제금액이 100원 이상, 계좌는 200원이상부터 결제가 가능합니다.",
            "INVALID_CARD_EXPIRATION" to "카드 정보를 다시 확인해주세요.",
            "INVALID_STOPPED_CARD" to "정지된 카드 입니다.",
            "EXCEED_MAX_DAILY_PAYMENT_COUNT" to "하루 결제 가능 횟수를 초과했습니다.",
            "INVALID_CARD_LOST_OR_STOLEN" to "분실 혹은 도난 카드입니다.",
            "INVALID_CARD_NUMBER" to "카드번호를 다시 확인해주세요.",
            "CARD_PROCESSING_ERROR" to "카드사에서 오류가 발생했습니다.",
            "EXCEED_MAX_ONE_DAY_WITHDRAW_AMOUNT" to "1일 출금 한도를 초과했습니다.",
            "EXCEED_MAX_ONE_TIME_WITHDRAW_AMOUNT" to "1회 출금 한도를 초과했습니다.",
            "EXCEED_MAX_MONTHLY_PAYMENT_AMOUNT" to "당월 결제 가능금액을 초과 하셨습니다.",
            "EXCEED_MAX_AMOUNT" to "거래금액 한도를 초과했습니다.",
            "INVALID_ACCOUNT_INFO_RE_REGISTER" to "유효하지 않은 계좌입니다. 계좌 재등록 후 시도해주세요.",
            "NOT_AVAILABLE_PAYMENT" to "결제가 불가능한 시간대입니다.",
            "UNAPPROVED_ORDER_ID" to "아직 승인되지 않은 주문입니다.",
            "REJECT_ACCOUNT_PAYMENT" to "잔액부족으로 결제에 실패했습니다.",
            "REJECT_CARD_PAYMENT" to "한도초과 혹은 잔액부족으로 결제에 실패했습니다",
            "REJECT_CARD_COMPANY" to "결제 승인이 거절되었습니다.",
            "EXCEED_MAX_AUTH_COUNT" to "최대 인증 횟수를 초과했습니다. 카드사로 문의해주세요.",
            "EXCEED_MAX_ONE_DAY_AMOUNT" to "일일 한도를 초과했습니다.",
            "NOT_AVAILABLE_BANK" to "은행 서비스 시간이 아닙니다.",
            "INVALID_PASSWORD" to "결제 비밀번호가 일치하지 않습니다.",
            "FDS_ERROR" to "[토스페이먼츠] 위험거래가 감지되어 결제가 제한됩니다. 발송된 문자에 포함된 링크를 통해 본인인증 후 결제가 가능합니다. (고객센터: 1644-8051)",
            "NOT_FOUND_PAYMENT" to "존재하지 않는 결제 정보 입니다.",
            "NOT_FOUND_PAYMENT_SESSION" to "결제 시간이 만료되어 결제 진행 데이터가 존재하지 않습니다.",
            "FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING" to "결제가 완료되지 않았어요. 다시 시도해주세요.",
            "FAILED_INTERNAL_SYSTEM_PROCESSING" to "PG사 처리 작업이 실패했습니다. 잠시 후 다시 시도해주세요.",
            "UNKNOWN_PAYMENT_ERROR" to "결제에 실패했어요. 같은 문제가 반복된다면 은행이나 카드사로 문의해주세요.",
        )
    }
}
