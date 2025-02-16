package org.fastcampus.applicationclient.cart.controller.dto.response

data class CartResponse(
    val storeId: String,
    val orderMenus: List<CartMenu>,
) {
    data class CartMenu(
        val cartId: Long,
        val menuId: String,
        val name: String,
        val imageUrl: String,
        val totalPrice: Long,
        val quantity: Long,
        val orderMenuOptionGroups: List<CartMenuOptionGroup>,
    ) {
        data class CartMenuOptionGroup(
            val id: String,
            val name: String,
            val orderMenuOptionIds: List<CartMenuOption>,
        ) {
            data class CartMenuOption(
                val id: String,
                val name: String,
            )
        }
    }
}
