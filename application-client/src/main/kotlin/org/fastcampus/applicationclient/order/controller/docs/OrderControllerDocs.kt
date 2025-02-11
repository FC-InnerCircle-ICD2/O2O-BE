package org.fastcampus.applicationclient.order.controller.docs

import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "주문 API", description = "주문 관련 API")
interface OrderControllerDocs : OrderCreation
