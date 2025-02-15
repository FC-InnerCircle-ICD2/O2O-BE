package org.fastcampus.applicationclient.cart.controller.dto.request

data class CartMenuInsertionRequest(
    val storeId: String,
    val orderMenu: OrderMenu,
) {
    data class OrderMenu(
        val menuId: String,
        val quantity: Long,
        val orderMenuOptionGroups: List<OrderMenuOptionGroup>,
    ) {
        data class OrderMenuOptionGroup(
            val id: String,
            val orderMenuOptionIds: List<String>,
        )
    }
}
