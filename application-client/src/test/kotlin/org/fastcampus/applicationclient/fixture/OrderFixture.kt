package org.fastcampus.applicationclient.fixture

import org.fastcampus.order.entity.Order
import org.fastcampus.order.entity.OrderMenu
import org.fastcampus.order.entity.OrderMenuOption
import org.fastcampus.order.entity.OrderMenuOptionGroup
import java.time.LocalDateTime

fun createOrderFixture(
    id: String = "order_123",
    storeId: String? = "store_456",
    userId: Long? = 789L,
    roadAddress: String? = "123 Test Street",
    jibunAddress: String? = "123 Test Village",
    detailAddress: String? = "Apt 101",
    tel: String? = "010-1234-5678",
    status: Order.Status = Order.Status.RECEIVE,
    orderTime: LocalDateTime = LocalDateTime.now(),
    orderSummary: String? = "2 items ordered",
    type: Order.Type = Order.Type.DELIVERY,
    paymentId: Long = 987L,
    isDeleted: Boolean = false,
    deliveryCompleteTime: LocalDateTime? = null,
    orderPrice: Long = 10000L,
    deliveryPrice: Long? = 3000L,
    paymentPrice: Long = 13000L,
): Order {
    return Order(
        id = id,
        storeId = storeId,
        storeName = "",
        storeImageThumbnail = "",
        userId = userId,
        roadAddress = roadAddress,
        jibunAddress = jibunAddress,
        detailAddress = detailAddress,
        tel = tel,
        status = status,
        orderTime = orderTime,
        orderSummary = orderSummary,
        type = type,
        paymentId = paymentId,
        isDeleted = isDeleted,
        deliveryCompleteTime = deliveryCompleteTime,
        orderPrice = orderPrice,
        deliveryPrice = deliveryPrice,
        paymentPrice = paymentPrice,
    )
}

fun createOrderMenuFixture(
    id: Long? = 1L,
    orderId: String? = "order_123",
    menuId: String = "menu_001",
    menuName: String = "Cheese Pizza",
    menuQuantity: Long = 2,
    menuPrice: Long = 10000L,
    totalPrice: Long = menuQuantity * menuPrice,
): OrderMenu {
    return OrderMenu(
        id = id,
        orderId = orderId,
        menuId = menuId,
        menuName = menuName,
        menuQuantity = menuQuantity,
        menuPrice = menuPrice,
        totalPrice = totalPrice,
    )
}

fun createOrderMenuOptionGroupFixture(id: Long? = 1L, orderMenuId: Long = 1L, orderMenuOptionGroupName: String = "Extra Toppings"): OrderMenuOptionGroup {
    return OrderMenuOptionGroup(
        id = id,
        orderMenuId = orderMenuId,
        orderMenuOptionGroupName = orderMenuOptionGroupName,
    )
}

fun createOrderMenuOptionFixture(id: Long? = 1L, orderMenuOptionGroupId: Long = 1L, menuOptionName: String = "Extra Cheese", menuOptionPrice: Long = 2000L): OrderMenuOption {
    return OrderMenuOption(
        id = id,
        orderMenuOptionGroupId = orderMenuOptionGroupId,
        menuOptionName = menuOptionName,
        menuOptionPrice = menuOptionPrice,
    )
}
