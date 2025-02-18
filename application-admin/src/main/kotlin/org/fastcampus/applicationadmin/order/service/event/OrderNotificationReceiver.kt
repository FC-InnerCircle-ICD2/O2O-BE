package org.fastcampus.applicationadmin.order.service.event

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.fastcampus.applicationadmin.order.controller.dto.OrderInquiryResponse
import org.fastcampus.applicationadmin.order.controller.dto.OrderMenuInquiryResponse
import org.fastcampus.applicationadmin.order.controller.dto.OrderMenuOptionGroupInquiryResponse
import org.fastcampus.applicationadmin.order.controller.dto.OrderMenuOptionInquiryResponse
import org.fastcampus.applicationadmin.sse.SseManager
import org.fastcampus.order.entity.OrderMenu
import org.fastcampus.order.entity.OrderMenuOptionGroup
import org.fastcampus.order.event.NotificationReceiver
import org.fastcampus.order.exception.OrderException
import org.fastcampus.order.repository.OrderMenuOptionGroupRepository
import org.fastcampus.order.repository.OrderMenuOptionRepository
import org.fastcampus.order.repository.OrderMenuRepository
import org.fastcampus.order.repository.OrderRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component("OrderNotificationReceiver")
class OrderNotificationReceiver(
    private val sseManager: SseManager,
    private val objectMapper: ObjectMapper,
    private val orderRepository: OrderRepository,
    private val orderMenuRepository: OrderMenuRepository,
    private val orderMenuOptionGroupRepository: OrderMenuOptionGroupRepository,
    private val orderMenuOptionRepository: OrderMenuOptionRepository,
) : NotificationReceiver {
    override fun handleMessage(message: String) {
        logger.debug("Received message {}", message)

        val readValue = objectMapper.readValue(message, object : TypeReference<Map<String, String>>() {}) ?: emptyMap()
        val ownerId = readValue["ownerId"] ?: "ownerId 없음"
        val orderId = readValue["orderId"] ?: "orderId 없음"

        sseManager.push(
            key = ownerId,
            eventType = "ORDER_NOTIFICATION",
            data = objectMapper.writeValueAsString(convertIntoOrderInquiryResponse(orderId)),
        )
    }

    private fun convertIntoOrderInquiryResponse(orderId: String): OrderInquiryResponse {
        val order = orderRepository.findById(orderId) ?: throw OrderException.OrderNotFound(orderId)
        val orderMenus = orderMenuRepository.findByOrderId(orderId)
        val orderMenuInquiryResponses = orderMenus.map { orderMenu -> this.convertIntoOrderMenuInquiryResponse(orderMenu) }
        return OrderInquiryResponse.from(order, orderMenuInquiryResponses)
    }

    private fun convertIntoOrderMenuInquiryResponse(orderMenu: OrderMenu): OrderMenuInquiryResponse {
        val orderMenuOptionGroups: List<OrderMenuOptionGroup> =
            orderMenuOptionGroupRepository.findByOrderMenuId(requireNotNull(orderMenu.id))
        val orderMenuOptionGroupInquiryResponses: List<OrderMenuOptionGroupInquiryResponse> =
            orderMenuOptionGroups.map { orderMenuOptionGroup -> convertIntoOrderMenuOptionGroupInquiryResponse(orderMenuOptionGroup) }
        return OrderMenuInquiryResponse.from(orderMenu, orderMenuOptionGroupInquiryResponses)
    }

    private fun convertIntoOrderMenuOptionGroupInquiryResponse(
        orderMenuOptionGroup: OrderMenuOptionGroup,
    ): OrderMenuOptionGroupInquiryResponse {
        val orderMenuOptionInquiryResponses: List<OrderMenuOptionInquiryResponse> = orderMenuOptionRepository
            .findByOrderMenuOptionGroupId(requireNotNull(orderMenuOptionGroup.id))
            .map { OrderMenuOptionInquiryResponse.from(it) }
        return OrderMenuOptionGroupInquiryResponse.from(orderMenuOptionGroup, orderMenuOptionInquiryResponses)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(OrderNotificationReceiver::class.java)
    }
}
