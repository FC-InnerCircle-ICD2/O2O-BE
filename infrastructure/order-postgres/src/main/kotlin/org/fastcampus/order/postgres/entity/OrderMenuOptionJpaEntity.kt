package org.fastcampus.order.postgres.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fastcampus.common.entity.BaseEntity
import org.fastcampus.order.entity.OrderMenuOption

/**
 * Created by brinst07 on 25. 1. 11..
 */
@Entity
@Table(name = "ORDER_MENU_OPTION")
class OrderMenuOptionJpaEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    val id: Long? = null,
    @Column(name = "ORDER_OPTION_GROUP_ID")
    val orderMenuOptionGroupId: Long,
    @Column(name = "MENU_OPTION_NAME")
    val menuOptionName: String,
    @Column(name = "MENU_OPTION_PRICE")
    val menuOptionPrice: Long,
) : BaseEntity()

fun OrderMenuOption.toJpaEntity() =
    OrderMenuOptionJpaEntity(
        id,
        orderMenuOptionGroupId,
        menuOptionName,
        menuOptionPrice,
    )

fun OrderMenuOptionJpaEntity.toModel() =
    OrderMenuOption(
        id,
        orderMenuOptionGroupId,
        menuOptionName,
        menuOptionPrice,
    )
