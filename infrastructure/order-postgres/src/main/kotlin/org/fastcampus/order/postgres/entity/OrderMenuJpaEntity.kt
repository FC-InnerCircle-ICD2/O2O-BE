package org.fastcampus.order.postgres.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fastcampus.common.entity.BaseEntity
import org.fastcampus.order.entity.OrderMenu

/**
 * Created by brinst07 on 25. 1. 11..
 */
@Entity
@Table(name = "ORDER_MENU")
class OrderMenuJpaEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    val id: Long? = null,
    @Column(name = "ORDER_ID")
    val orderId: String?,
    @Column(name = "MENU_ID")
    val menuId: String,
    @Column(name = "MENU_NAME")
    val menuName: String,
    @Column(name = "MENU_QUANTITY")
    val menuQuantity: Long,
    @Column(name = "MENU_PRICE")
    val menuPrice: Long,
    @Column(name = "TOTAL_PRICE")
    val totalPrice: Long,
) : BaseEntity()

fun OrderMenu.toJpaEntity() =
    OrderMenuJpaEntity(
        id,
        orderId,
        menuId,
        menuName,
        menuQuantity,
        menuPrice,
        totalPrice,
    )

fun OrderMenuJpaEntity.toModel() =
    OrderMenu(
        id,
        orderId,
        menuId,
        menuName,
        menuQuantity,
        menuPrice,
        totalPrice,
    )
