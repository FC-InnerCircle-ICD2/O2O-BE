package org.fastcampus.applicationadmin.sse.docs

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.fastcampus.applicationadmin.config.security.dto.AuthMember
import org.springframework.http.MediaType
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

interface OrderNotification {
    @Operation(
        summary = "SSE 연결 및 주문알림 수신",
        description = """
            점주앱 서버에 SSE 연결 요청을 합니다.
            (서버 커넥션 60초설정. 60초마다 웹 API가 자동 재연결)

            결제 승인시 알림을 받습니다.
        """,
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
                                          "orderId": "c735a637-621d-4926-9811-909dc2584cf9",
                                          "orderName": "새우 로제 파스타 외 2개",
                                          "orderStatus": "NEW",
                                          "orderType": "DELIVERY",
                                          "orderTime": "2025-01-16T10:00:00",
                                          "totalPrice": 39400,
                                          "totalMenuCount": 3,
                                          "roadAddress": "서울특별시 구구구 동동동 123-4",
                                          "jibunAddress": "서울특별시 구구구 경리단길 123",
                                          "detailAddress": "401호",
                                          "excludingSpoonAndFork": false,
                                          "requestToRider": "문 앞에 두고 벨 눌러주세요.",
                                          "orderMenuInquiryResponses": [
                                            {
                                              "id": 1,
                                              "orderId": "c735a637-621d-4926-9811-909dc2584cf9",
                                              "menuId": "d5010526-60ac-4656-b105-f591a2013435",
                                              "menuName": "[주문폭주] 투움바 파스타 1",
                                              "menuQuantity": 1,
                                              "menuPrice": 12400,
                                              "totalPrice": 12900,
                                              "orderMenuOptionGroupInquiryResponses": [
                                                {
                                                  "id": 1,
                                                  "orderMenuId": 1,
                                                  "orderMenuOptionGroupName": "피클 선택",
                                                  "orderMenuOptionInquiryResponses": [
                                                    {
                                                      "id": 1,
                                                      "orderMenuOptionGroupId": 1,
                                                      "menuOptionName": "상큼한 피클",
                                                      "menuOptionPrice": 500
                                                    }
                                                  ]
                                                }
                                              ]
                                            },
                                            {
                                              "id": 2,
                                              "orderId": "c735a637-621d-4926-9811-909dc2584cf9",
                                              "menuId": "d5010526-60ac-4656-b105-f591a2011235",
                                              "menuName": "[주문폭주] 감바스",
                                              "menuQuantity": 2,
                                              "menuPrice": 13000,
                                              "totalPrice": 27000,
                                              "orderMenuOptionGroupInquiryResponses": [
                                                {
                                                  "id": 2,
                                                  "orderMenuId": 2,
                                                  "orderMenuOptionGroupName": "빵 선택",
                                                  "orderMenuOptionInquiryResponses": [
                                                    {
                                                      "id": 2,
                                                      "orderMenuOptionGroupId": 2,
                                                      "menuOptionName": "마늘빵",
                                                      "menuOptionPrice": 500
                                                    }
                                                  ]
                                                }
                                              ]
                                            }
                                          ]
                                        }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun connectSse(authMember: AuthMember): SseEmitter
}
