package org.fastcampus.applicationadmin.dashboard.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.fastcampus.applicationadmin.dashboard.dto.response.OrdersSummaryResponse
import org.fastcampus.applicationadmin.dashboard.dto.response.SalesSummaryResponse
import org.fastcampus.applicationadmin.dashboard.dto.response.Type
import org.fastcampus.applicationadmin.fixture.createOrderFixture
import org.fastcampus.order.entity.Order
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.store.repository.StoreRepository
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.time.LocalDate
import java.time.LocalDateTime

class DashboardServiceTest {
    private val orderRepository: OrderRepository = mockk()
    private val storeRepository: StoreRepository = mockk()

    private val dashboardService = DashboardService(
        orderRepository = orderRepository,
        storeRepository = storeRepository,
    )

    @Test
    fun `getDashboard returns correct Sales dashboard response`() {
        // given
        val ownerId = 1L
        val storeId = "store_123"
        val startDate = LocalDate.of(2025, 2, 1)
        val endDate = LocalDate.of(2025, 2, 15)
        val type = Type.SALES.name

        every { storeRepository.findByOwnerId(ownerId.toString()) } returns storeId

        // 테스트용 Order 데이터 (3건 중 COMPLETED 2건만 SALES 계산 대상)
        val order1 = createOrderFixture(
            id = "order_1",
            storeId = storeId,
            status = Order.Status.COMPLETED,
            orderPrice = 15000L,
            paymentPrice = 15000L,
            orderTime = LocalDateTime.of(2025, 2, 2, 10, 0, 0),
        )
        val order2 = createOrderFixture(
            id = "order_2",
            storeId = storeId,
            status = Order.Status.WAIT, // WAIT는 제외됨
            orderPrice = 10000L,
            paymentPrice = 10000L,
            orderTime = LocalDateTime.of(2025, 2, 2, 11, 0, 0),
        )
        val order3 = createOrderFixture(
            id = "order_3",
            storeId = storeId,
            status = Order.Status.COMPLETED,
            orderPrice = 20000L,
            paymentPrice = 20000L,
            orderTime = LocalDateTime.of(2025, 2, 3, 9, 30, 0),
        )

        every {
            orderRepository.findAllByStoreIdAndOrderTimeBetweenAndStatusNot(
                storeId,
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59),
                Order.Status.WAIT,
            )
        } returns listOf(order1, order2, order3)

        // when
        val dashboardResponse = dashboardService.getDashboard(ownerId, startDate, endDate, type)
            ?: error("dashboardResponse is null")

        // then
        // SALES 타입은 COMPLETED 주문만 사용함 (order1, order3)
        // totalPrice = 15000 + 20000 = 35000
        // avgPrice = 35000 / 2 = 17500
        // 단순 평균 로직이므로, 일별, 시간별 평균도 17500로 계산됨 (예시)
        expectThat(dashboardResponse) {
            get { type } isEqualTo Type.SALES.name
            get { summary } isEqualTo SalesSummaryResponse(
                totalPrice = 35000L,
                avgPrice = 17500L,
                avgPricePerDay = 17500L,
                avgPricePerTime = 17500L,
            )
            // data에 COMPLETED 주문만 변환되어 들어있으므로 2건이어야 함
            get { data.size } isEqualTo 2
        }

        verify(exactly = 1) {
            storeRepository.findByOwnerId(ownerId.toString())
        }
        verify(exactly = 1) {
            orderRepository.findAllByStoreIdAndOrderTimeBetweenAndStatusNot(
                storeId,
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59),
                Order.Status.WAIT,
            )
        }
    }

    @Test
    fun `getDashboard returns correct Orders dashboard response`() {
        // given
        val ownerId = 1L
        val storeId = "store_123"
        val startDate = LocalDate.of(2025, 2, 1)
        val endDate = LocalDate.of(2025, 2, 15)
        val type = Type.ORDERS.name

        every { storeRepository.findByOwnerId(ownerId.toString()) } returns storeId

        // 테스트용 Order 데이터 - WAIT 상태 제외, 모두 포함
        val order1 = createOrderFixture(
            id = "order_1",
            storeId = storeId,
            status = Order.Status.RECEIVE,
            orderPrice = 15000L,
            paymentPrice = 15000L,
            orderTime = LocalDateTime.of(2025, 2, 2, 10, 0, 0),
        )
        val order2 = createOrderFixture(
            id = "order_2",
            storeId = storeId,
            status = Order.Status.CANCEL, // 취소 주문
            orderPrice = 10000L,
            paymentPrice = 10000L,
            orderTime = LocalDateTime.of(2025, 2, 2, 11, 0, 0),
        )
        val order3 = createOrderFixture(
            id = "order_3",
            storeId = storeId,
            status = Order.Status.COMPLETED,
            orderPrice = 20000L,
            paymentPrice = 20000L,
            orderTime = LocalDateTime.of(2025, 2, 3, 9, 30, 0),
        )

        every {
            orderRepository.findAllByStoreIdAndOrderTimeBetweenAndStatusNot(
                storeId,
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59),
                Order.Status.WAIT,
            )
        } returns listOf(order1, order2, order3)

        // when
        val dashboardResponse = dashboardService.getDashboard(ownerId, startDate, endDate, type)
            ?: error("dashboardResponse is null")

        // then
        // ORDERS 타입은 모든 주문(WAIT 제외)을 사용함: 3건
        // cancelOrders는 order2가 취소 상태이므로 1건
        // avgOrderPerDay = totalOrder / days, 여기서는 테스트 환경에 따라 3건/15일 = 0 (정수 나눗셈)
        expectThat(dashboardResponse) {
            get { type } isEqualTo Type.ORDERS.name
            get { summary } isEqualTo OrdersSummaryResponse(
                totalOrder = 3L,
                avgOrderPerDay = 0L,
                cancelOrders = 1L,
                cancelRate = (1.0 / 3.0) * 100.0,
            )
            get { data.size } isEqualTo 3
        }

        verify(exactly = 1) {
            storeRepository.findByOwnerId(ownerId.toString())
        }
        verify(exactly = 1) {
            orderRepository.findAllByStoreIdAndOrderTimeBetweenAndStatusNot(
                storeId,
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59),
                Order.Status.WAIT,
            )
        }
    }
}
