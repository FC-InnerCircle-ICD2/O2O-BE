package org.fastcampus.applicationadmin.service

import org.fastcampus.applicationadmin.fixture.createAuthMember
import org.fastcampus.applicationadmin.fixture.createMember
import org.fastcampus.applicationadmin.fixture.createOrderFixture
import org.fastcampus.applicationadmin.fixture.createOrderMenuFixture
import org.fastcampus.applicationadmin.fixture.createOrderMenuOptionFixture
import org.fastcampus.applicationadmin.fixture.createOrderMenuOptionGroupFixture
import org.fastcampus.applicationadmin.fixture.createStore
import org.fastcampus.applicationadmin.order.controller.dto.OrderInquiryResponse
import org.fastcampus.applicationadmin.order.service.OrderService
import org.fastcampus.common.dto.OffSetBasedDTO
import org.fastcampus.member.entity.Member
import org.fastcampus.member.repository.MemberRepository
import org.fastcampus.order.entity.Order
import org.fastcampus.order.exception.OrderException
import org.fastcampus.order.repository.OrderLockManager
import org.fastcampus.order.repository.OrderMenuOptionGroupRepository
import org.fastcampus.order.repository.OrderMenuOptionRepository
import org.fastcampus.order.repository.OrderMenuRepository
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.store.repository.StoreRepository
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isNotNull
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class OrderServiceTest {
    @Mock private lateinit var orderRepository: OrderRepository

    @Mock private lateinit var orderMenuRepository: OrderMenuRepository

    @Mock private lateinit var orderMenuOptionGroupRepository: OrderMenuOptionGroupRepository

    @Mock private lateinit var orderMenuOptionRepository: OrderMenuOptionRepository

    @Mock private lateinit var storeRepository: StoreRepository

    @Mock private lateinit var memberRepository: MemberRepository

    @Mock private lateinit var orderLockManager: OrderLockManager

    @InjectMocks private lateinit var orderService: OrderService

    @Test
    fun `should return orders by storeId and period`() {
        // given
        val storeId: String = UUID.randomUUID().toString()
        val status = listOf(Order.ClientStatus.NEW)
        val startDate = LocalDate.of(2025, 1, 16)
        val endDate = LocalDate.of(2025, 1, 16)
        val page = 0
        val size = 5
        val userId = 1L

        val member: Member = createMember(id = userId)
        val order = createOrderFixture(storeId = storeId, userId = userId)
        val orderMenu = createOrderMenuFixture(orderId = order.id)
        val orderMenuOptionGroup = createOrderMenuOptionGroupFixture(orderMenuId = requireNotNull(orderMenu.id))
        val orderMenuOption = createOrderMenuOptionFixture(orderMenuOptionGroupId = requireNotNull(orderMenuOptionGroup.id))

        `when`(orderRepository.findByStoreIdAndStatusesWithPeriod(storeId, status.map { it.toOrderStatus() }, startDate.atStartOfDay(), endDate.atTime(23, 59, 59), page, size))
            .thenReturn(OffSetBasedDTO(listOf(order), page, 0, 1, false))
        `when`(storeRepository.findByOwnerId(member.id.toString())).thenReturn(storeId)
        `when`(memberRepository.findById(userId)).thenReturn(member)
        `when`(orderMenuRepository.findByOrderId(orderId = order.id))
            .thenReturn(listOf(orderMenu))
        `when`(orderMenuOptionGroupRepository.findByOrderMenuId(orderMenuId = orderMenu.id!!))
            .thenReturn(listOf(orderMenuOptionGroup))
        `when`(orderMenuOptionRepository.findByOrderMenuOptionGroupId(orderMenuOptionGroupId = orderMenuOptionGroup.id!!))
            .thenReturn(listOf(orderMenuOption))

        // when
        val result: OffSetBasedDTO<OrderInquiryResponse> = orderService.getOrdersByStatusesWithPeriod(userId, status, startDate, endDate, page, size)

        // then
        expectThat {
            expectThat(result.content).isNotNull()
            expectThat(result.currentPage).isEqualTo(page)
            expectThat(result.totalPages).isEqualTo(0)
            expectThat(result.totalItems).isEqualTo(1)
            expectThat(result.hasNext).isFalse()
        }

        verify(orderRepository).findByStoreIdAndStatusesWithPeriod(storeId, status.map { it.toOrderStatus() }, startDate.atStartOfDay(), endDate.atTime(23, 59, 59), page, size)
        verify(orderMenuRepository).findByOrderId(orderId = order.id)
        verify(orderMenuOptionGroupRepository).findByOrderMenuId(orderMenuId = orderMenu.id!!)
        verify(orderMenuOptionRepository).findByOrderMenuOptionGroupId(orderMenuOptionGroupId = orderMenuOptionGroup.id!!)
        verify(storeRepository).findByOwnerId(member.id.toString())
        verify(memberRepository).findById(userId)
    }

    @Test
    fun `must change order status to accept when admin accept order`() {
        // given
        val owner = createAuthMember()
        val store = createStore(ownerId = owner.id.toString())
        val order = createOrderFixture().copy(status = Order.Status.RECEIVE, storeId = store.id)

        `when`(orderRepository.findById(order.id)).thenReturn(order)
        `when`(storeRepository.findById(order.storeId!!)).thenReturn(store)
        `when`(orderRepository.save(order)).thenReturn(order)
        `when`(orderLockManager.lock(eq(order.id), any<() -> Unit>())).thenReturn(order.accept())

        // when
        orderService.acceptOrder(order.id, owner.id)

        // then
        verify(orderRepository).findById(order.id)
        verify(storeRepository).findById(order.storeId!!)
        verify(orderRepository).save(order)
        verify(orderLockManager).lock(eq(order.id), any<() -> Unit>())
        expectThat(order.status).isEqualTo(Order.Status.ACCEPT)
    }

    @Test
    @Disabled
    fun `must throw exception when admin try to accept order witch has not receive status`() {
        // given
        val owner = createAuthMember()
        val store = createStore(ownerId = owner.id.toString())
        val order = createOrderFixture().copy(status = Order.Status.CANCEL)

        `when`(orderRepository.findById(order.id)).thenReturn(order)
        `when`(storeRepository.findById(order.storeId!!)).thenReturn(store)

        // when & then
        expectThrows<OrderException.OrderCanNotAccept> {
            orderService.acceptOrder(order.id, owner.id)
        }.and {
            get { orderId }.isEqualTo(order.id)
            get { message }.isEqualTo("주문 수락이 불가능한 주문입니다.")
        }
        verify(orderRepository).findById(order.id)
        verify(storeRepository).findById(order.storeId!!)
        verify(orderRepository, never()).save(order)
    }
}
