package org.fastcampus.order.event

data class OrderNotification(
    val orderId: String,
)

data class OrderNotificationMessage(
    val orderId: String,
    val orderName: String,
    val orderStatus: String,
    val orderType: String,
    val orderTime: Long,
    val totalPrice: Long,
    val totalMenuCount: Long,
    val orderDetail: List<OrderDetail>,
) {
    data class OrderDetail(
        val id: String,
        val price: Long,
        val menuName: String,
        val menuQuantity: Long,
        val menuPrice: Long,
        val menuOptionGroups: List<MenuOptionGroup>,
    ) {
        data class MenuOptionGroup(
            val id: String,
            val menuOptionGroupName: String,
            val menuOptionPrice: Long,
        )
    }
}
