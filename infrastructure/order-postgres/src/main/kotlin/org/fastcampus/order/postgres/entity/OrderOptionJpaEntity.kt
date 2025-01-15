package org.fastcampus.order.postgres.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fastcampus.common.entity.BaseEntity
import org.fastcampus.order.entity.Order
import org.fastcampus.order.entity.OrderOption

/**
 * Created by brinst07 on 25. 1. 11..
 */
@Entity
@Table(name = "TB_ORDER_OPTION")
class OrderOptionJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    val id: Long? = null,
    @Column(name = "ORDER_OPTION_GROUP_ID")
    val orderOptionGroupId: Long?,
    @Column(name = "MENU_OPTION_NAME")
    val menuOptionName: String?,
    @Column(name = "MENU_OPTION_PRICE")
    val menuOptionPrice: Long?,
) : BaseEntity()

fun OrderOption.toJpaEntity() =
    OrderOptionJpaEntity(
        id,
        orderOptionGroupId,
        menuOptionName,
        menuOptionPrice
    )

fun OrderOptionJpaEntity.toModel() =
    OrderOption(
        id,
        orderOptionGroupId,
        menuOptionName,
        menuOptionPrice
    )
