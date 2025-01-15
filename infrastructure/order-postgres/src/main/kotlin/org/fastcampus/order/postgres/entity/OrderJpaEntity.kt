package org.fastcampus.order.postgres.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fastcampus.common.entity.BaseEntity
import org.fastcampus.order.entity.Order

/**
 * Created by brinst07 on 25. 1. 11..
 */
@Entity
@Table(name = "TB_ORDER")
class OrderJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    val id: Long? = null,
    @Column(name = "STORE_ID")
    val storeId: Long?,
    @Column(name = "ROAD_ADDRESS")
    val roadAddress: String?,
    @Column(name = "JIBUN_ADDRESS")
    val jibunAddress: String?,
    @Column(name = "STATUS")
    val status: String?,
    @Column(name = "ORDER_TIME")
    val orderTime: String?,
    @Column(name = "TYPE")
    val type: String?,
    @Column(name = "IS_DELETED")
    val isDeleted: String?,
    @Column(name = "DELIVERY_TIME")
    val deliveryTime: String?,
) : BaseEntity()

fun Order.toJpaEntity() =
    OrderJpaEntity(
        id,
        storeId,
        roadAddress,
        jibunAddress,
        status,
        orderTime,
        type,
        isDeleted,
        deliveryTime,
    )

fun OrderJpaEntity.toModel() =
    Order(
        id,
        storeId,
        roadAddress,
        jibunAddress,
        status,
        orderTime,
        type,
        isDeleted,
        deliveryTime,
    )
