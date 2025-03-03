package org.fastcampus.applicationclient.order.service

import org.fastcampus.applicationclient.fixture.createMember
import org.fastcampus.applicationclient.fixture.createOrderFixture
import org.fastcampus.applicationclient.order.service.event.OrderCancellationEvent
import org.fastcampus.order.entity.Order
import org.fastcampus.order.exception.OrderException
import org.fastcampus.order.repository.OrderLockManager
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.payment.service.RefundManager
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.context.ApplicationEventPublisher
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

@ExtendWith(MockitoExtension::class)
class OrderCancellationServiceTest {
    @Mock lateinit var orderRepository: OrderRepository

    @Mock lateinit var refundManager: RefundManager

    @Mock lateinit var eventPublisher: ApplicationEventPublisher

    @Mock lateinit var orderLockManager: OrderLockManager

    @InjectMocks lateinit var orderCancellationService: OrderCancellationService

    @Test
    @Disabled
    fun `cancel order`() {
        // given
        val user = createMember(id = 1L)
        val order = createOrderFixture().copy(id = "order_123", status = Order.Status.RECEIVE, userId = user.id)

        `when`(orderRepository.findById(order.id)).thenReturn(order)
        doNothing().`when`(refundManager).refundOrder(order.id, order.orderPrice, order.paymentId)
        doNothing().`when`(eventPublisher).publishEvent(OrderCancellationEvent(storeId = order.storeId!!, orderId = order.id))
        whenever(orderLockManager.lock(eq(order.id), any<() -> Order>())).thenReturn(order)

        // when
        orderCancellationService.cancelOrder(order.id, user.id!!)

        verify(orderRepository).findById(order.id)
        expectThat(order.status).isEqualTo(Order.Status.CANCEL)
    }

    @Test
    @Disabled
    fun `must throw exception when order is cancelled which has not RECEIVE status`() {
        // given
        val user = createMember(id = 1L)
        var order = createOrderFixture(userId = user.id)
        order = order.copy(status = Order.Status.REFUSE)

        `when`(orderRepository.findById(order.id)).thenReturn(order)
        `when`(orderLockManager.lock(eq(order.id), any<() -> Unit>())).thenAnswer {
            { order.cancel() }
        }

        // when & then
        expectThrows<OrderException.OrderCanNotCancelled> {
            orderCancellationService.cancelOrder(order.id, user.id!!)
        }.and {
            get { orderId }.isEqualTo(order.id)
            get { message }.isEqualTo("해당 주문은 취소할 수 없습니다.")
        }
        verify(orderRepository).findById(order.id)
        verify(orderRepository, never()).save(order)
    }
}
