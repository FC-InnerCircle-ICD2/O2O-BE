package org.fastcampus.applicationclient.order.service

import org.fastcampus.applicationclient.order.controller.dto.response.OrderDetailResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderMenuOptionGroupResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderMenuOptionResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderMenuResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderResponse
import org.fastcampus.common.dto.CursorBasedDTO
import org.fastcampus.order.repository.OrderMenuOptionGroupRepository
import org.fastcampus.order.repository.OrderMenuOptionRepository
import org.fastcampus.order.repository.OrderMenuRepository
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.payment.repository.PaymentRepository
import org.fastcampus.store.repository.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val storeRepository: StoreRepository,
    private val paymentRepository: PaymentRepository,
    private val orderMenuRepository: OrderMenuRepository,
    private val orderMenuOptionGroupRepository: OrderMenuOptionGroupRepository,
    private val orderMenuOptionRepository: OrderMenuOptionRepository,
) {
    @Transactional(readOnly = true)
    fun getOrders(userId: Long, keyword: String, page: Int, size: Int): CursorBasedDTO<OrderResponse> {
        val orders = orderRepository.findByUserId(userId, page, size)
        return CursorBasedDTO(
            isEnd = orders.isEnd,
            totalCount = orders.totalCount,
            content = orders.content.map { order ->
                OrderResponse(
                    storeId = order.storeId,
                    storeName = "",
                    imageThumbnail = "",
                    orderId = order.id,
                    status = mapOf("code" to order.status.code, "desc" to order.status.desc),
                    orderTime = order.orderTime,
                    orderSummary = order.orderSummary,
                    deliveryCompleteTime = order.deliveryCompleteTime,
                    paymentPrice = order.paymentPrice,
                )
            },
        )
    }

    @Transactional(readOnly = true)
    fun getOrder(orderId: String): OrderDetailResponse {
        val order = requireNotNull(orderRepository.findById(orderId))
        val payment = requireNotNull(paymentRepository.findById(order.paymentId))
        val orderMenus = orderMenuRepository.findByOrderId(order.id)
        return OrderDetailResponse(
            orderId = order.id,
            status = mapOf("code" to order.status.code, "desc" to order.status.desc),
            orderTime = order.orderTime,
            isDeleted = order.isDeleted,
            tel = order.tel,
            roadAddress = order.roadAddress,
            jibunAddress = order.jibunAddress,
            detailAddress = order.detailAddress,
            orderPrice = order.orderPrice,
            deliveryPrice = order.deliveryPrice,
            deliveryCompleteTime = order.deliveryCompleteTime,
            paymentPrice = order.paymentPrice,
            paymentId = order.paymentId,
            paymentType = mapOf("code" to payment.type.code, "desc" to payment.type.desc),
            type = mapOf("code" to order.type.code, "desc" to order.type.desc),
            orderMenus = orderMenus.map { orderMenu ->
                val orderMenuOptionGroups = orderMenuOptionGroupRepository.findByOrderMenuId(requireNotNull(orderMenu.id))
                OrderMenuResponse(
                    id = orderMenu.id,
                    menuId = orderMenu.menuId,
                    menuName = orderMenu.menuName,
                    menuQuantity = orderMenu.menuQuantity,
                    menuPrice = orderMenu.menuPrice,
                    totalPrice = orderMenu.totalPrice,
                    orderMenuOptionGroups = orderMenuOptionGroups.map { orderMenuOptionGroup ->
                        val orderMenuOptions = orderMenuOptionRepository
                            .findByOrderMenuOptionGroupId(requireNotNull(orderMenuOptionGroup.id))
                        OrderMenuOptionGroupResponse(
                            id = orderMenuOptionGroup.id,
                            orderMenuId = orderMenuOptionGroup.orderMenuId,
                            orderMenuOptionGroupName = orderMenuOptionGroup.orderMenuOptionGroupName,
                            orderMenuOptions = orderMenuOptions.map { orderMenu ->
                                OrderMenuOptionResponse(
                                    id = orderMenu.id,
                                    orderMenuOptionGroupId = orderMenu.orderMenuOptionGroupId,
                                    menuOptionName = orderMenu.menuOptionName,
                                    menuOptionPrice = orderMenu.menuOptionPrice,
                                )
                            },
                        )
                    },
                )
            },
        )
    }
}
