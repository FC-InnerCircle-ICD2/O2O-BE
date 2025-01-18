package org.fastcampus.applicationclient.order.service

import org.fastcampus.applicationclient.order.controller.dto.response.OrderResponse
import org.fastcampus.common.dto.CursorBasedDTO
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.store.entity.Store
import org.fastcampus.store.repository.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val storeRepository: StoreRepository,
) {
    @Transactional(readOnly = true)
    fun getOrders(userId: Long, keyword: String, page: Int, size: Int): CursorBasedDTO<OrderResponse> {
        val orders = orderRepository.findByUserId(userId, page, size)
        return CursorBasedDTO(
            isEnd = orders.isEnd,
            totalCount = orders.totalCount,
            content = orders.content.map { order ->
                val store: Store? = storeRepository.findById(requireNotNull(order.storeId))
                OrderResponse(
                    storeId = order.storeId,
                    storeName = store?.name,
                    imageThumbnail = store?.imageThumbnail,
                    orderId = order.id,
                    status = mapOf("code" to order.status.code, "desc" to order.status.desc),
                    orderTime = order.orderTime,
                    orderSummary = order.orderSummary,
                    isDeleted = order.isDeleted,
                    deliveryCompleteTime = order.deliveryCompleteTime,
                    tel = order.tel,
                    roadAddress = order.roadAddress,
                    jibunAddress = order.jibunAddress,
                    detailAddress = order.detailAddress,
                    paymentPrice = order.paymentPrice,
                    orderPrice = order.orderPrice,
                    deliveryPrice = order.deliveryPrice,
                    type = mapOf("code" to order.type.code, "desc" to order.type.desc),
                )
            },
        )
    }
}
