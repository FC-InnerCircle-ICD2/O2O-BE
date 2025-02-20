package org.fastcampus.applicationclient.order.controller.dto.request

import org.fastcampus.order.entity.Order
import org.fastcampus.payment.entity.Payment

data class OrderCreationRequest(
    val storeId: String,
    val roadAddress: String?,
    val jibunAddress: String?,
    val detailAddress: String,
    val excludingSpoonAndFork: Boolean? = true,
    val requestToRider: String? = null,
    val orderType: Order.Type,
    val paymentType: Payment.Type,
    val orderMenus: List<OrderMenu>,
) {
    data class OrderMenu(
        val id: String,
        val quantity: Long,
        val orderMenuOptionGroups: List<OrderMenuOptionGroup>,
    ) {
        data class OrderMenuOptionGroup(
            val id: String,
            val orderMenuOptionIds: List<String>,
        )
    }
}
