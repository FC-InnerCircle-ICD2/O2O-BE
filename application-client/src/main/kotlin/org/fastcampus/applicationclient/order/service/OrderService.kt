package org.fastcampus.applicationclient.order.service

import org.fastcampus.applicationclient.order.controller.dto.response.OrderDetailResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderMenuOptionGroupResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderMenuOptionResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderMenuResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderResponse
import org.fastcampus.common.dto.CursorDTO
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
    fun getOrders(userId: Long, keyword: String, page: Int, size: Int): CursorDTO<OrderResponse> {
        val orders = orderRepository.findByUserId(userId, if (page == 0) 0 else page, size)
        return CursorDTO(
            content = orders.content.map { order ->
                val store = storeRepository.findById(requireNotNull(order.storeId))
                OrderResponse(
                    storeId = order.storeId,
                    storeName = store?.name,
                    imageThumbnail = store?.imageThumbnail,
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
