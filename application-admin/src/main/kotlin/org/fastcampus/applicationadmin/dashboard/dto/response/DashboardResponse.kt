package org.fastcampus.applicationadmin.dashboard.dto.response

import org.fastcampus.order.entity.Order
import java.time.LocalDateTime

sealed class SummaryResponse

data class DashboardResponse(
    val type: Type, // 요청 타입에 따라
    val summary: SummaryResponse, // SALES이면 SalesSummaryResponse, ORDERS이면 OrdersSummaryResponse
    val data: List<OrderHistory?>,
)

data class SalesSummaryResponse(
    val totalPrice: Long,
    val avgPrice: Long,
    val avgPricePerTime: Long,
    val avgPricePerDay: Long,
) : SummaryResponse()

data class OrdersSummaryResponse(
    val totalOrder: Long,
    val avgOrderPerDay: Long,
    val cancelOrders: Long,
    val cancelRate: Double,
) : SummaryResponse()

data class OrderHistory(
    val orderTime: LocalDateTime,
    val menu: String,
    val price: Long,
    val status: Order.ClientStatus,
)

enum class Type {
    SALES,
    ORDERS,
}

fun Order.toOrderHistory() =
    orderSummary?.let {
        OrderHistory(
            orderTime = orderTime,
            menu = it,
            price = orderPrice,
            status = status.toClientStatus(),
        )
    }
