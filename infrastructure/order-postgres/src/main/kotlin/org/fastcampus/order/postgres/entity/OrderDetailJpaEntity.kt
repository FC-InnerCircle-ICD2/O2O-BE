package org.fastcampus.order.postgres.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fastcampus.common.entity.BaseEntity
import org.fastcampus.order.entity.OrderDetail
import org.fastcampus.order.entity.OrderOptionGroup

/**
 * Created by brinst07 on 25. 1. 11..
 */
@Entity
@Table(name = "TB_ORDER_DETAIL")
class OrderDetailJpaEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    val id: Long? = null,
    @Column(name = "ORDER_ID")
    val orderId: String?,
    @Column(name = "PRICE")
    val price: Long?,
    @Column(name = "MENU_NAME")
    val menuName: String?,
    @Column(name = "MENU_QUANTITY")
    val menuQuantity: Long?,
    @Column(name = "MENU_PRICE")
    val menuPrice: Long?,
) : BaseEntity()

fun OrderDetail.toJpaEntity() =
    OrderDetailJpaEntity(
        id,
        orderId,
        price,
        menuName,
        menuQuantity,
        menuPrice
    )

fun OrderDetailJpaEntity.toModel() =
    OrderDetail(
        id,
        orderId,
        price,
        menuName,
        menuQuantity,
        menuPrice
    )
