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
    fun getDashboard(ownerId: Long, startDate: LocalDate, endDate: LocalDate, type: String): DashboardResponse? =
        storeRepository.findByOwnerId(ownerId.toString()).let {
            val storeId: String = storeRepository.findByOwnerId(ownerId.toString())
            val startOfDay = startDate.atStartOfDay()
            val endOfDay = endDate.atTime(23, 59, 59)

            val allOrders =
                orderRepository.findAllByStoreIdAndOrderTimeBetweenAndStatusIn(
                    storeId,
                    startOfDay,
                    endOfDay,
                    listOf(Order.Status.COMPLETED, Order.Status.CANCEL),
                )
            val completedOrders = allOrders.filter { it.status == Order.Status.COMPLETED }
            val completedAndCancelledOrders = allOrders.filter { it.status == Order.Status.COMPLETED || it.status == Order.Status.CANCEL }

            when (type) {
                Type.SALES.name -> {
                    val totalPrice = completedOrders.sumOf { it.paymentPrice }
                    // "기간 일수"를 활용한, 전 기간 기반의 일 평균 매출
                    val totalDays = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1
                    val avgPrice = if (completedOrders.isNotEmpty()) totalPrice / completedOrders.size else 0L
                    val avgPricePerDay = if (totalDays > 0) totalPrice / totalDays else 0L
                    val avgPricePerTime = totalPrice / 24

                    DashboardResponse(
                        type = Type.SALES,
                        summary = SalesSummaryResponse(
                            totalPrice = totalPrice,
                            avgPrice = avgPrice,
                            avgPricePerDay = avgPricePerDay,
                            avgPricePerTime = avgPricePerTime,
                        ),
                        data = completedOrders.map { order ->
                            order.toOrderHistory().also {
                                logger.debug { "원본 주문 데이터: $order, 변환된 OrderHistory: $it" }
                            }
                        },
                    )
                }

                Type.ORDERS.name -> {
                    // 주문 관련 통계 계산
                    val totalOrder = completedAndCancelledOrders.size.toLong()
                    val days = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1
                    val avgOrderPerDay = if (days > 0) totalOrder / days else 0L
                    val cancelOrders =
                        completedAndCancelledOrders.count { it.status.toClientStatus() == Order.ClientStatus.CANCEL }.toLong()
                    val cancelRate = if (totalOrder > 0) (cancelOrders.toDouble() / totalOrder) * 100.0 else 0.0

                    DashboardResponse(
                        type = Type.ORDERS,
                        summary = OrdersSummaryResponse(
                            totalOrder = totalOrder,
                            avgOrderPerDay = avgOrderPerDay,
                            cancelOrders = cancelOrders,
                            cancelRate = cancelRate,
                        ),
                        data = completedAndCancelledOrders.map { order ->
                            order.toOrderHistory().also {
                                logger.debug { "원본 주문 데이터: $order, 변환된 OrderHistory: $it" }
                            }
                        },
                    )
                }

                else -> {
                    throw IllegalArgumentException("예상치 못한 타입입니다: $type")
                }
            }
        }

    // 일별 평균 매출 계산 예시 (날짜별 그룹핑)
    private fun computeAvgPricePerDay(orders: List<Order>): Long =
        orders.groupBy { it.orderTime.toLocalDate() }
            .map { (_, orders) -> orders.sumOf { it.orderPrice } }
            .takeIf { it.isNotEmpty() }
            ?.average()?.toLong() ?: 0L

    // 시간대별 평균 매출 계산 예시 (시간별 그룹핑, 단순 예시)
    private fun computeAvgPricePerTime(orders: List<Order>): Long =
        orders.groupBy { it.orderTime.hour }
            .map { (_, orders) -> orders.sumOf { it.orderPrice } }
            .takeIf { it.isNotEmpty() }
            ?.average()?.toLong() ?: 0L
}
