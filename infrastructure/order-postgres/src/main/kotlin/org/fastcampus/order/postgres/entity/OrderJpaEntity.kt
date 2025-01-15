package org.fastcampus.order.postgres.entity

import jakarta.persistence.*
import org.fastcampus.common.entity.BaseEntity
import org.fastcampus.order.entity.Order
import java.time.LocalDateTime
import java.util.*

/**
 * Created by brinst07 on 25. 1. 11..
 */
@Entity
@Table(name = "ORDER")
class OrderJpaEntity(
    @Column(name = "ID")
    val id: String = UUID.randomUUID().toString(),
    @Column(name = "STORE_ID")
    val storeId: String,
    @Column(name = "USER_ID")
    val userId: Long,
    @Column(name = "ROAD_ADDRESS")
    val roadAddress: String?,
    @Column(name = "JIBUN_ADDRESS")
    val jibunAddress: String?,
    @Column(name = "DETAIL_ADDRESS")
    val detailAddress: String?,
    @Column(name = "TEL")
    val tel: String?,
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    val status: Order.Status,
    @Column(name = "ORDER_TIME")
    val orderTime: LocalDateTime,
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    val type: Order.Type,
    @Column(name = "PAYMENT_ID")
    val paymentId: Long,
    @Column(name = "IS_DELETED")
    val isDeleted: Boolean,
    @Column(name = "DELIVERY_COMPLETE_TIME")
    val deliveryCompleteTime: LocalDateTime?,
    @Column(name = "ORDER_PRICE")
    val orderPrice: Long?,
    @Column(name = "DELIVERY_PRICE")
    val deliveryPrice: Long?,
    @Column(name = "PAYMENT_PRICE")
    val paymentPrice: Long?,
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
        status,
        orderTime,
        type,
        paymentId,
        isDeleted,
        deliveryCompleteTime,
        orderPrice,
        deliveryPrice,
        paymentPrice,
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
        status,
        orderTime,
        type,
        paymentId,
        isDeleted,
        deliveryCompleteTime,
        orderPrice,
        deliveryPrice,
        paymentPrice,
    )
