package org.fastcampus.applicationadmin.dashboard.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.fastcampus.applicationadmin.dashboard.dto.response.DashboardResponse
import org.fastcampus.applicationadmin.dashboard.dto.response.OrdersSummaryResponse
import org.fastcampus.applicationadmin.dashboard.dto.response.SalesSummaryResponse
import org.fastcampus.applicationadmin.dashboard.dto.response.Type
import org.fastcampus.applicationadmin.dashboard.dto.response.toOrderHistory
import org.fastcampus.order.entity.Order
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.store.repository.StoreRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private val logger = KotlinLogging.logger {}

@Service
class DashboardService(
    private val orderRepository: OrderRepository,
    private val storeRepository: StoreRepository,
) {
    fun getDashboard(ownerId: Long, startDate: LocalDate, endDate: LocalDate, type: String): DashboardResponse? {
        val storeId: String = storeRepository.findByOwnerId(ownerId.toString())
        val startOfDay = startDate.atStartOfDay()
        val endOfDay = endDate.atTime(23, 59, 59)

        val allOrders = orderRepository.findAllByStoreIdAndOrderTimeBetweenAndStatusNot(storeId, startOfDay, endOfDay, Order.Status.WAIT)
        val completedOrders = allOrders.filter { it.status == Order.Status.COMPLETED }

        return when (type) {
            Type.SALES.name -> {
                val totalPrice = completedOrders.sumOf { it.paymentPrice }
                val avgPrice = if (completedOrders.isNotEmpty()) totalPrice / completedOrders.size else 0L
                val avgPricePerDay = computeAvgPricePerDay(completedOrders)
                val avgPricePerTime = computeAvgPricePerTime(completedOrders)

                DashboardResponse(
                    type = Type.SALES,
                    summary = SalesSummaryResponse(
                        totalPrice = totalPrice,
                        avgPrice = avgPrice,
                        avgPricePerDay = avgPricePerDay,
                        avgPricePerTime = avgPricePerTime,
                    ),
                    data = completedOrders.map { order ->
                        val orderHistory = order.toOrderHistory()
                        logger.debug { "원본 주문 데이터: $order, 변환된 OrderHistory: $orderHistory" }
                        orderHistory
                    },
                )
            }

            Type.ORDERS.name -> {
                // 주문 관련 통계 계산
                val totalOrder = allOrders.size.toLong()
                val days = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1
                val avgOrderPerDay = if (days > 0) totalOrder / days else 0L
                val cancelOrders = allOrders.count { it.status.toClientStatus() == Order.ClientStatus.CANCEL }.toLong()
                val cancelRate = if (totalOrder > 0) (cancelOrders.toDouble() / totalOrder) * 100.0 else 0.0

                DashboardResponse(
                    type = Type.ORDERS,
                    summary = OrdersSummaryResponse(
                        totalOrder = totalOrder,
                        avgOrderPerDay = avgOrderPerDay,
                        cancelOrders = cancelOrders,
                        cancelRate = cancelRate,
                    ),
                    data = allOrders.map { order ->
                        val orderHistory = order.toOrderHistory()
                        logger.debug { "원본 주문 데이터: $order, 변환된 OrderHistory: $orderHistory" }
                        orderHistory
                    },
                )
            }

            else -> {
                throw IllegalArgumentException("예상치 못한 타입입니다: $type")
            }
        }
    }
}

// 일별 평균 매출 계산 예시 (날짜별 그룹핑)
private fun computeAvgPricePerDay(orders: List<Order>): Long {
    val ordersByDay = orders.groupBy { it.orderTime.toLocalDate() }
    val dailyTotals = ordersByDay.map { (_, orders) -> orders.sumOf { it.orderPrice } }
    return if (dailyTotals.isNotEmpty()) dailyTotals.average().toLong() else 0L
}

// 시간대별 평균 매출 계산 예시 (시간별 그룹핑, 단순 예시)
private fun computeAvgPricePerTime(orders: List<Order>): Long {
    val ordersByHour = orders.groupBy { it.orderTime.hour }
    val hourlyTotals = ordersByHour.map { (_, orders) -> orders.sumOf { it.orderPrice } }
    return if (hourlyTotals.isNotEmpty()) hourlyTotals.average().toLong() else 0L
}
