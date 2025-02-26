package org.fastcampus.applicationclient.order.service

import org.fastcampus.applicationclient.aop.OrderMetered
import org.fastcampus.applicationclient.order.controller.dto.response.OrderDetailResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderMenuOptionGroupResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderMenuOptionResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderMenuResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderResponse
import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.order.repository.OrderDetailRepository
import org.fastcampus.order.repository.OrderMenuOptionGroupRepository
import org.fastcampus.order.repository.OrderMenuOptionRepository
import org.fastcampus.order.repository.OrderMenuRepository
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.payment.repository.PaymentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val orderMenuRepository: OrderMenuRepository,
    private val orderMenuOptionGroupRepository: OrderMenuOptionGroupRepository,
    private val orderMenuOptionRepository: OrderMenuOptionRepository,
    private val orderDetailRepository: OrderDetailRepository,
) {
    @Transactional(readOnly = true)
    @OrderMetered
    fun getOrders(userId: Long, keyword: String, page: Int, size: Int): CursorDTO<OrderResponse> {
        val orders = orderRepository.findByUserIdExcludingWaitStatus(userId, keyword, page, size)
        return CursorDTO(
            content = orders.content.map { order ->
                OrderResponse(
                    storeId = order.storeId,
                    storeName = order.storeName,
                    imageThumbnail = order.storeImageThumbnail,
                    orderId = order.id,
                    status = mapOf("code" to order.status.code, "desc" to order.status.desc),
                    orderTime = order.orderTime,
                    orderSummary = order.orderSummary,
                    deliveryCompleteTime = order.deliveryCompleteTime,
                    paymentPrice = order.paymentPrice,
                )
            },
            nextCursor = orders.nextCursor?.plus(1),
        )
    }

    @Transactional(readOnly = true)
    @OrderMetered
    fun getOrder(orderId: String): OrderDetailResponse {
        orderDetailRepository.findById(orderId)?.let { orderDetail ->
            return OrderDetailResponse.from(orderDetail)
        }
        val order = requireNotNull(orderRepository.findById(orderId))
        val payment = requireNotNull(paymentRepository.findById(order.paymentId))
        val orderMenus = orderMenuRepository.findByOrderId(order.id)
        return OrderDetailResponse.from(
            order,
            payment.type,
            orderMenus.map { orderMenu ->
                val orderMenuOptionGroups = orderMenuOptionGroupRepository.findByOrderMenuId(requireNotNull(orderMenu.id))
                OrderMenuResponse.from(
                    orderMenu,
                    orderMenuOptionGroups.map { orderMenuOptionGroup ->
                        val orderMenuOptions = orderMenuOptionRepository
                            .findByOrderMenuOptionGroupId(requireNotNull(orderMenuOptionGroup.id))
                        OrderMenuOptionGroupResponse.from(
                            orderMenuOptionGroup,
                            orderMenuOptions.map { orderMenu -> OrderMenuOptionResponse.from(orderMenu) },
                        )
                    },
                )
            },
        )
    }
}
