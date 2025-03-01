package org.fastcampus.applicationclient.payment.service

import org.fastcampus.applicationclient.aop.OrderMetered
import org.fastcampus.applicationclient.order.service.event.OrderDetailStatusEvent
import org.fastcampus.applicationclient.order.service.event.OrderNotificationEvent
import org.fastcampus.cart.repository.CartRepository
import org.fastcampus.order.entity.Order
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.payment.entity.Payment
import org.fastcampus.payment.exception.PaymentException
import org.fastcampus.payment.gateway.PaymentGatewayResponse
import org.fastcampus.payment.repository.PaymentRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val paymentGatewayFactory: PaymentGatewayFactory,
) {
    @Transactional
    fun savePaymentKey(userId: Long, orderId: String, paymentKey: String) {
        val order = findOrder(userId, orderId)
        val payment = findPayment(order.paymentId)
        // PG 키가 없을때만 업데이트 - TODO 동시에 UPDATE 일어날 수 있지만,, 일단 구현
        if (!StringUtils.hasText(payment.pgKey)) {
            paymentRepository.save(payment.copy(pgKey = paymentKey))
        }
    }

    @Transactional
    @OrderMetered
    fun approveOrderPayment(userId: Long, orderId: String, amount: Long) {
        // TODO 동시요청은 고려 안하고 일단 구현
        val order = findOrder(userId, orderId)

        // 주문 상태가 WAIT 아니라면 진행중단
        if (order.status != Order.Status.WAIT) {
            throw PaymentException.OrderStatusIsNotWaiting(order.id)
        }

        // PG 승인요청시 금액 불일치라면 승인이 안 될 것이지만 불필요한 API 호출 방지
        if (amount != order.paymentPrice) {
            throw PaymentException.IncorrectAmount("reqAmount: [$amount] | orderPayment: [${order.paymentPrice}]")
        }

        val payment = findPayment(order.paymentId)

        // PG 결제승인 요청
        val paymentGateway = paymentGatewayFactory.getPaymentGateway(payment.type)
        val result = paymentGateway.approve(
            paymentKey = payment.pgKey ?: throw PaymentException.PgKeyNotExists(payment.id.toString()),
            orderId = order.id,
            amount = order.paymentPrice,
        )

        // 결제승인이 실패시 예외 - TODO 결제 상태를 새로운 트랜잭션으로 FAILED(결제실패) 상태로 바꿔야 할지? 초기 WAIT(결제대기) 상태로 남길지?
        if (result.status != PaymentGatewayResponse.Status.DONE) {
            throw PaymentException.PGFailed(payment.id.toString(), result.message)
        }

        // 결제완료 변경
        paymentRepository.save(payment.copy(status = Payment.Status.COMPLETED))

        // 주문접수 상태 변경
        val updatedOrder = orderRepository.save(order.copy(status = Order.Status.RECEIVE))

        // 장바구니 삭제
        cartRepository.removeByUserId(updatedOrder.userId!!)

        // 점주에게 주문 알림 - 트랜잭션 커밋이후 비동기 전송
        eventPublisher.publishEvent(OrderNotificationEvent(updatedOrder))

        eventPublisher.publishEvent(OrderDetailStatusEvent(updatedOrder.id, updatedOrder.status))
    }

    private fun findPayment(paymentId: Long): Payment {
        return paymentRepository.findById(paymentId)
            ?: throw PaymentException.PaymentNotFound(paymentId.toString())
    }

    private fun findOrder(userId: Long, orderId: String): Order {
        val order = orderRepository.findById(orderId)
            ?: throw PaymentException.OrderNotFound(orderId)
        if (order.userId != userId) {
            throw PaymentException.UserNotMatching("login: [$userId] | orderUser: [${order.userId}]")
        }
        return order
    }
}
