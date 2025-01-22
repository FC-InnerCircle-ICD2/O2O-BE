package org.fastcampus.order.entity

import org.fastcampus.order.repository.OrderMenuOptionGroupRepository
import org.fastcampus.order.repository.OrderMenuOptionRepository
import org.fastcampus.order.repository.OrderMenuRepository
import java.time.ZoneOffset

data class OrderReceivedEvent(
    val orderId: String,
    val orderName: String,
    val orderStatus: String,
    val orderType: String,
    val orderTime: Long,
    val orderTotalPrice: Long,
    val orderTotalMenuCount: Long,
    val roadAddress: String,
    val jibunAddress: String,
    val detailAddress: String,
    val orderMenus: List<OrderMenu>,
) {
    data class OrderMenu(
        val id: Long,
        val menuName: String,
        val menuQuantity: Long,
        val menuPrice: Long,
        val totalPrice: Long,
        val orderMenuOptionGroups: List<OrderMenuOptionGroup>,
    ) {
        data class OrderMenuOptionGroup(
            val id: Long,
            val orderMenuOptionGroupName: String,
            val orderMenuOptions: List<OrderMenuOption>,
        ) {
            data class OrderMenuOption(
                val id: Long,
                val orderMenuOptionName: String,
                val orderMenuOptionPrice: Long,
            )
        }
    }

    companion object {
        fun of(
            order: Order,
            orderMenuRepository: OrderMenuRepository,
            orderMenuOptionGroupRepository: OrderMenuOptionGroupRepository,
            orderMenuOptionRepository: OrderMenuOptionRepository,
        ): OrderReceivedEvent {
            val orderMenus = orderMenuRepository.findByOrderId(order.id)

            return OrderReceivedEvent(
                orderId = order.id,
                orderName = order.orderSummary ?: "알 수 없음",
                orderStatus = order.status.name,
                orderType = order.type.name,
                orderTime = order.orderTime.toEpochSecond(ZoneOffset.UTC),
                orderTotalPrice = order.orderPrice,
                orderTotalMenuCount = orderMenus.sumOf { it.menuQuantity },
                roadAddress = order.roadAddress ?: "알 수 없음",
                jibunAddress = order.jibunAddress ?: "알 수 없음",
                detailAddress = order.detailAddress ?: "알 수 없음",
                orderMenus = orderMenus.map { orderMenu ->
                    OrderMenu(
                        id = orderMenu.id!!,
                        menuName = orderMenu.menuName,
                        menuQuantity = orderMenu.menuQuantity,
                        menuPrice = orderMenu.menuPrice,
                        totalPrice = orderMenu.totalPrice,
                        orderMenuOptionGroups =
                            orderMenuOptionGroupRepository.findByOrderMenuId(orderMenu.id)
                                .map { orderMenuOptionGroup ->
                                    OrderMenu.OrderMenuOptionGroup(
                                        id = orderMenuOptionGroup.id!!,
                                        orderMenuOptionGroupName = orderMenuOptionGroup.orderMenuOptionGroupName,
                                        orderMenuOptions =
                                            orderMenuOptionRepository.findByOrderMenuOptionGroupId(orderMenuOptionGroup.id)
                                                .map { orderMenuOption ->
                                                    OrderMenu.OrderMenuOptionGroup.OrderMenuOption(
                                                        id = orderMenuOption.id!!,
                                                        orderMenuOptionName = orderMenuOption.menuOptionName,
                                                        orderMenuOptionPrice = orderMenuOption.menuOptionPrice,
                                                    )
                                                },
                                    )
                                },
                    )
                },
            )
        }
    }
}
