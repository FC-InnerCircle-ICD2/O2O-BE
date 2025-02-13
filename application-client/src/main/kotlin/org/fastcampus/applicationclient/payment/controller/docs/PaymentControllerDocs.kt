package org.fastcampus.applicationclient.payment.controller.docs

import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "결제 API", description = "결제 관련 API")
interface PaymentControllerDocs : PaymentApprove
