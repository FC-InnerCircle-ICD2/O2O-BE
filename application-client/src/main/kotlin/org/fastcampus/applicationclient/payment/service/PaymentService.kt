package org.fastcampus.applicationclient.payment.service

import org.fastcampus.applicationclient.aop.OrderMetered
import org.fastcampus.applicationclient.order.service.event.OrderNotificationEvent
import org.fastcampus.applicationclient.payment.controller.dto.request.OrderPaymentApproveRequest
import org.fastcampus.cart.repository.CartRepository
import org.fastcampus.order.entity.Order
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.payment.exception.PaymentException
import org.fastcampus.payment.repository.PaymentRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {
    @Transactional
    @OrderMetered
    fun approveOrderPayment(userId: Long, request: OrderPaymentApproveRequest) {
        val order = orderRepository.findById(request.orderId)
            ?: throw PaymentException.OrderNotFound(request.orderId)

        if (order.userId != userId) {
            throw PaymentException.UserNotMatching("login: [$userId] | orderUser: [${order.userId}]")
        }

        // TODO - 주문 상태가 WAIT 인지 확인

        if (request.amount != order.paymentPrice) {
            throw PaymentException.IncorrectAmount("reqAmount: [${request.amount}] | orderPayment: [${order.paymentPrice}]")
        }

        // TODO - PG 승인 구현

        // TODO - Payment Status 추가, 결제완료시 상태 변경
        paymentRepository.findById(order.paymentId)
            ?: throw PaymentException.PaymentNotFound(order.paymentId.toString())

        // 주문접수 상태 변경
        orderRepository.save(order.copy(status = Order.Status.RECEIVE))

        // 장바구니 삭제
        cartRepository.removeByUserId(order.userId!!)

        // 점주에게 주문 알림 - 트랜잭션 커밋이후 비동기 전송
        eventPublisher.publishEvent(OrderNotificationEvent(order))
    }
}
