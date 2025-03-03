package org.fastcampus.applicationclient.order.controller.docs

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.order.controller.dto.request.OrderCreationRequest
import org.fastcampus.applicationclient.order.controller.dto.response.OrderCreationResponse
import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.http.MediaType

interface OrderCreation {
    @Operation(
        summary = "주문 생성",
        description = """
            주문 항목들을 검증하고, 주문과 주문에 대한 결제 정보를 생성합니다.
            PG 결제 전 호출하여 주문 번호와 주문 이름, 결제 총액 정보를 얻습니다.
            사용자 위경도 정보를 받아 가게와 주문지 거리가 너무나 멀다면 생성하지 않습니다.

            storeId (필수)
              - 가게 ID
            roadAddress (roadAddress 또는 jibunAddress 중 1개 필수)
              - 도로명주소
            jibunAddress (roadAddress 또는 jibunAddress 중 1개 필수)
              - 지번주소
            detailAddress (필수)
              - 상세주소
            excludingSpoonAndFork (선택)
              - 수저포크 빼주세요 여부. true - 빼기(기본값) / false - 빼지 않기
            requestToRider (선택)
              - 라이더 요청사항
            orderType (필수)
              - 주문타입 'DELIVERY' 또는 'PACKING'
            paymentType (필수)
              - 결제타입 'TOSS_PAY' 또는 'KAKAO_PAY'
            orderMenus[] (필수)
              - 주문할 메뉴정보 배열
            orderMenus[].id (필수)
              - 주문할 메뉴 id
            orderMenus[].quantity (필수)
              - 구매수량
            orderMenus[].orderMenuOptionGroups[]
              - 메뉴의 옵션그룹정보 배열
            orderMenus[].orderMenuOptionGroups[].id
              - 옵션그룹 id
            orderMenus[].orderMenuOptionGroups[].orderMenuOptionIds[]
              - 옵션 id 목록
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
                                      "storeId": "26466355",
                                      "roadAddress": "서울 동대문구 왕산로 123",
                                      "jibunAddress": "서울 동대문구 전농동 123-45",
                                      "detailAddress": "O2O아파트 123호",
                                      "excludingSpoonAndFork": false,
                                      "requestToRider": "문 앞에 두고 벨 눌러주세요.",
                                      "orderType": "DELIVERY",
                                      "paymentType": "TOSS_PAY",
                                      "orderMenus": [
                                        {
                                          "id": "9e15c3da-cae8-4c25-8b4e-ae21a926f072",
                                          "quantity": 2,
                                          "orderMenuOptionGroups": [
                                            {
                                              "id": "62f5ab82-6f9f-4344-964c-9a09a568df62",
                                              "orderMenuOptionIds": [
                                                "c5d18e3d-e643-49d6-b089-3dbb5a148800"
                                              ]
                                            }
                                          ]
                                        },
                                        {
                                          "id": "9e15c3da-cae8-4c25-8b4e-ae21a926f072",
                                          "quantity": 1,
                                          "orderMenuOptionGroups": [
                                            {
                                              "id": "62f5ab82-6f9f-4344-964c-9a09a568df62",
                                              "orderMenuOptionIds": [
                                                "fa72f6e8-1cd4-4ee7-a4ca-61167daaa3fe"
                                              ]
                                            }
                                          ]
                                        },
                                        {
                                          "id": "51c20adf-9962-48b7-aaec-65a867d3c0c6",
                                          "quantity": 1,
                                          "orderMenuOptionGroups": [
                                            {
                                              "id": "62bd01d0-01a3-4865-852e-a422fbf505a1",
                                              "orderMenuOptionIds": [
                                                "81b31574-9cd9-49ae-ad1b-3c6603f35f51"
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
                                          "data": {
                                            "orderId": "0f82760f-b7a9-40e9-98e8-19e84ff8a7cd",
                                            "orderSummary": "아메리카노 외 2개",
                                            "totalPrice": 40500
                                          }
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
                                          "message": "필수 옵션그룹이 누락되었습니다.",
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
    fun createOrder(
        userLat: Double,
        userLng: Double,
        orderCreationRequest: OrderCreationRequest,
        authMember: AuthMember,
    ): APIResponseDTO<OrderCreationResponse>
}
