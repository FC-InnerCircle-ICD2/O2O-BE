package org.fastcampus.applicationadmin.order.service

import org.fastcampus.applicationadmin.order.controller.dto.OrderInquiryResponse
import org.fastcampus.applicationadmin.order.controller.dto.OrderMenuInquiryResponse
import org.fastcampus.applicationadmin.order.controller.dto.OrderMenuOptionGroupInquiryResponse
import org.fastcampus.applicationadmin.order.controller.dto.OrderMenuOptionInquiryResponse
import org.fastcampus.common.dto.OffSetBasedDTO
import org.fastcampus.order.entity.Order
import org.fastcampus.order.entity.OrderMenu
import org.fastcampus.order.entity.OrderMenuOptionGroup
import org.fastcampus.order.exception.OrderException
import org.fastcampus.order.repository.OrderMenuOptionGroupRepository
import org.fastcampus.order.repository.OrderMenuOptionRepository
import org.fastcampus.order.repository.OrderMenuRepository
import org.fastcampus.order.repository.OrderRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderMenuRepository: OrderMenuRepository,
    private val orderMenuOptionGroupRepository: OrderMenuOptionGroupRepository,
    private val orderMenuOptionRepository: OrderMenuOptionRepository,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(OrderService::class.java)
    }

    @Transactional(readOnly = true)
    fun getOrdersByStoreIdAndStatusWithPeriod(
        storeId: String,
        status: Order.Status,
        startDate: LocalDate,
        endDate: LocalDate,
        page: Int,
        size: Int,
    ): OffSetBasedDTO<OrderInquiryResponse> {
        val startOfDay = startDate.atStartOfDay()
        val endOfDay = endDate.atTime(23, 59, 59)
        val orders = orderRepository.findByStoreIdAndStatusWithPeriod(storeId, status, startOfDay, endOfDay, page, size)

        logger.info("order ids of inquiry result: {}", orders.content.joinToString(",") { it.id })

        val orderInquiryResponses: List<OrderInquiryResponse> =
            orders.content.map { order -> this.convertIntoOrderInquiryResponse(order) }
        return OffSetBasedDTO(
            content = orderInquiryResponses,
            currentPage = orders.currentPage,
            totalPages = orders.totalPages,
            totalItems = orders.totalItems,
            hasNext = orders.hasNext,
        )
    }

    fun acceptOrder(orderId: String) {
        val order = orderRepository.findById(orderId) ?: throw OrderException.OrderNotFound(orderId)
        order.accept()
        orderRepository.save(order)
    }

    fun refuseOrder(orderId: String) {
        val order = orderRepository.findById(orderId) ?: throw OrderException.OrderNotFound(orderId)
        order.refuse()
        orderRepository.save(order)
    }

    private fun convertIntoOrderInquiryResponse(order: Order): OrderInquiryResponse {
        val orderMenus: List<OrderMenu> = orderMenuRepository.findByOrderId(order.id)
        val orderMenuInquiryResponses: List<OrderMenuInquiryResponse> =
            orderMenus.map { orderMenu -> this.convertIntoOrderMenuInquiryResponse(orderMenu) }
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
}
