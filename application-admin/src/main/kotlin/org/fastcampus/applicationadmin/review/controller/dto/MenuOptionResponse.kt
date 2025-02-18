package org.fastcampus.applicationadmin.review.controller.dto

import org.fastcampus.order.entity.OrderMenuOption

data class MenuOptionResponse(
    val menuOptionName: String,
    val menuOptionQuantity: Int,
) {
    companion object {
        fun of(orderMenuOption: List<OrderMenuOption>): List<MenuOptionResponse> {
            return orderMenuOption.groupBy { it.menuOptionName }
                .map { (name, options) -> MenuOptionResponse(name, options.size) }
        }
    }
}
