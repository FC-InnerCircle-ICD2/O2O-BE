package org.fastcampus.order.postgres.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fastcampus.common.entity.BaseEntity
import org.fastcampus.order.entity.Order
import org.fastcampus.order.entity.OrderStatus
import org.fastcampus.order.entity.OrderType
import org.fastcampus.order.entity.PaymentType
import java.time.LocalDateTime
import java.util.UUID

/**
 * Created by brinst07 on 25. 1. 11..
 */
@Entity
@Table(name = "TB_ORDER")
class OrderJpaEntity(
    @Column(name = "ID")
    val id: String = UUID.randomUUID().toString(),
    @Column(name = "STORE_ID")
    val storeId: Long?,
    @Column(name = "USER_ID")
    val userId: Long?,
    @Column(name = "ROAD_ADDRESS")
    val roadAddress: String?,
    @Column(name = "JIBUN_ADDRESS")
    val jibunAddress: String?,
    @Column(name = "DETAIL_ADDRESS")
    val detailAddress: String?,
    @Column(name = "TEL")
    val tel: String?,
    @Enumerated(EnumType.STRING)
    @Column(name = "ORDER_STATUS")
    val orderStatus: OrderStatus?,
    @Column(name = "ORDER_TIME")
    val orderTime: LocalDateTime?,
    @Enumerated(EnumType.STRING)
    @Column(name = "ORDER_TYPE")
    val orderType: OrderType?,
    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_TYPE")
    val paymentType: PaymentType?,
    @Column(name = "IS_DELETED")
    val isDeleted: Boolean,
    @Column(name = "DELIVERY_TIME")
    val deliveryTime: LocalDateTime?,
    @Column(name = "DELIVERY_PRICE")
    val deliveryPrice: Long?
) : BaseEntity()

fun Order.toJpaEntity() =
    OrderJpaEntity(
        id,
        storeId,
        userId,
        roadAddress,
        jibunAddress,
        detailAddress,
        tel,
        orderStatus,
        orderTime,
        orderType,
        paymentType,
        isDeleted,
        deliveryTime,
        deliveryPrice
    )

fun OrderJpaEntity.toModel() =
    Order(
        id,
        storeId,
        userId,
        roadAddress,
        jibunAddress,
        detailAddress,
        tel,
        orderStatus,
        orderTime,
        orderType,
        paymentType,
        isDeleted,
        deliveryTime,
        deliveryPrice
    )
