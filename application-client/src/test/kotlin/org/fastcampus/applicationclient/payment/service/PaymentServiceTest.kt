package org.fastcampus.applicationclient.payment.service

import org.fastcampus.applicationclient.order.service.event.OrderNotificationEvent
import org.fastcampus.applicationclient.payment.controller.dto.request.OrderPaymentApproveRequest
import org.fastcampus.cart.repository.CartRepository
import org.fastcampus.order.entity.Order
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.payment.entity.Payment
import org.fastcampus.payment.exception.PaymentException
import org.fastcampus.payment.gateway.PaymentGateway
import org.fastcampus.payment.gateway.PaymentGatewayResponse
import org.fastcampus.payment.repository.PaymentRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationEventPublisher
import org.springframework.core.env.Environment
import strikt.api.expectThrows
import java.time.LocalDateTime

class PaymentServiceTest {
    private lateinit var paymentService: PaymentService
    private lateinit var paymentRepository: PaymentRepository
    private lateinit var orderRepository: OrderRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var eventPublisher: ApplicationEventPublisher
    private lateinit var paymentGateway: PaymentGateway
    private lateinit var paymentGatewayFactory: PaymentGatewayFactory

    @BeforeEach
    fun init() {
        paymentRepository = mock(PaymentRepository::class.java)
        orderRepository = mock(OrderRepository::class.java)
        cartRepository = mock(CartRepository::class.java)
        eventPublisher = mock(ApplicationEventPublisher::class.java)
        paymentGatewayFactory = PaymentGatewayFactory(
            applicationContext = mock(ApplicationContext::class.java),
            environment = mock(Environment::class.java),
        )
        paymentGateway = mock(PaymentGateway::class.java)
        paymentService = PaymentService(
            paymentRepository = paymentRepository,
            orderRepository = orderRepository,
            cartRepository = cartRepository,
            eventPublisher = eventPublisher,
            paymentGatewayFactory = paymentGatewayFactory,
        )
    }

    @Test
    fun `approveOrderPayment execute normally`() {
        // given
        val order = createOrder()
        val payment = createPayment(order)
        val request = OrderPaymentApproveRequest(
            paymentKey = payment.pgKey ?: "123",
            orderId = order.id,
            amount = order.paymentPrice,
        )

        `when`(orderRepository.findById(request.orderId))
            .thenReturn(order)
        `when`(orderRepository.save(any()))
            .thenReturn(order)
        `when`(paymentRepository.findById(order.paymentId))
            .thenReturn(payment)
        `when`(paymentGateway.approve(payment.pgKey ?: "", request.orderId, request.amount))
            .thenReturn(PaymentGatewayResponse(status = PaymentGatewayResponse.Status.DONE))

        // when
        paymentService.approveOrderPayment(order.userId!!, request.orderId, request.amount)

        // then
        Mockito.verify(orderRepository).findById(request.orderId)
        Mockito.verify(paymentRepository).findById(order.paymentId)
        Mockito.verify(orderRepository).save(order.copy(status = Order.Status.RECEIVE))
        Mockito.verify(eventPublisher).publishEvent(OrderNotificationEvent(order))
    }

    @Test
    fun `approveOrderPayment must throw OrderNotFound when order not found`() {
        // given
        val request = OrderPaymentApproveRequest(
            paymentKey = "",
            orderId = "test",
            amount = 1_000L,
        )

        `when`(orderRepository.findById(request.orderId))
            .thenReturn(null)

        // when, then
        expectThrows<PaymentException.OrderNotFound> { paymentService.approveOrderPayment(1L, request.orderId, request.amount) }
    }

    @Test
    fun `approveOrderPayment must throw UserNotMatching when userId not matched`() {
        // given
        val order = createOrder()
        val request = OrderPaymentApproveRequest(
            paymentKey = "",
            orderId = order.id,
            amount = order.paymentPrice,
        )

        `when`(orderRepository.findById(request.orderId))
            .thenReturn(order)

        // when, then
        expectThrows<PaymentException.UserNotMatching> { paymentService.approveOrderPayment(-1L, request.orderId, request.amount) }
    }

    @Test
    fun `approveOrderPayment must throw IncorrectAmount when amount not equals request`() {
        // given
        val order = createOrder()
        val request = OrderPaymentApproveRequest(
            paymentKey = "",
            orderId = order.id,
            amount = 1L,
        )

        `when`(orderRepository.findById(request.orderId))
            .thenReturn(order)

        // when, then
        expectThrows<PaymentException.IncorrectAmount> { paymentService.approveOrderPayment(order.userId!!, request.orderId, request.amount) }
    }

    @Test
    fun `approveOrderPayment must throw PaymentNotFound when payment not found`() {
        // given
        val order = createOrder()

        val request = OrderPaymentApproveRequest(
            paymentKey = "",
            orderId = order.id,
            amount = order.paymentPrice,
        )

        `when`(orderRepository.findById(request.orderId))
            .thenReturn(order)
        `when`(paymentRepository.findById(order.paymentId))
            .thenReturn(null)

        // when, then
        expectThrows<PaymentException.PaymentNotFound> { paymentService.approveOrderPayment(order.userId!!, request.orderId, request.amount) }
    }

    private fun createPayment(order: Order): Payment {
        return Payment(
            id = order.paymentId,
            type = Payment.Type.TOSS_PAY,
            paymentPrice = order.paymentPrice,
            pgKey = "123",
        )
    }

    private fun createOrder(): Order {
        return Order(
            id = "orderId",
            userId = 1L,
            orderPrice = 1_000L,
            paymentPrice = 1_000L,
            storeId = "storeId",
            storeName = "",
            storeImageThumbnail = "",
            roadAddress = "roadAddress",
            jibunAddress = "jibunAddress",
            detailAddress = "detailAddress",
            status = Order.Status.WAIT,
            tel = "010-1234-5678",
            orderTime = LocalDateTime.now(),
            orderSummary = "아메리카노 외 1개",
            type = Order.Type.DELIVERY,
            paymentId = 1L,
            isDeleted = false,
            deliveryCompleteTime = null,
            deliveryPrice = 0L,
        )
    }
}
