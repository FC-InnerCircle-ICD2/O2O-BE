package org.fastcampus.applicationclient.payment.controller.docs

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.payment.controller.dto.request.OrderPaymentApproveRequest
import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.http.MediaType

interface PaymentApprove {
    @Operation(
        summary = "결제 승인",
        description = """
            PG 결제 이후 콜백으로 받은 정보를 이용해 주문의 결제를 승인 요청합니다.
            정상 승인 이후 점주 주문 알림이 전송됩니다.

            paymentKey (필수)
              - PG Key
            orderId (필수)
              - 주문 ID
            amount (필수)
              - 결제금액
        """,
        requestBody = RequestBody(
            content = [
                Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = [
                        ExampleObject(
                            name = "요청 예시",
                            value = """
                                    {
                                      "paymentKey": "",
                                      "orderId": "y251a739-b52d-2fe4-1b62-045384011515",
                                      "amount": 21900
                                    }
                            """,
                        ),
                    ],
                ),
            ],
        ),
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "성공 응답 예시",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = [
                            ExampleObject(
                                value = """
                                        {
                                          "status": 200,
                                          "message": "OK",
                                          "data": null
                                        }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "실패 응답 예시",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = [
                            ExampleObject(
                                value = """
                                        {
                                          "status": 400,
                                          "message": "결제 정보를 찾을 수 없습니다.",
                                          "data": null
                                        }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun approveOrderPayment(orderPaymentApproveRequest: OrderPaymentApproveRequest, authMember: AuthMember): APIResponseDTO<Void>
}
