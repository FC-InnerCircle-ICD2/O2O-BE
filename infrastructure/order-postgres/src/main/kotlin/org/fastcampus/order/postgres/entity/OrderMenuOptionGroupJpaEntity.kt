package org.fastcampus.order.postgres.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fastcampus.common.entity.BaseEntity
import org.fastcampus.order.entity.OrderMenuOptionGroup

@Entity
@Table(name = "ORDER_MENU_OPTION_GROUP")
class OrderMenuOptionGroupJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    val id: Long? = null,
    @Column(name = "ORDER_MENU_ID")
    val orderMenuId: Long,
    @Column(name = "ORDER_MENU_OPTION_GROUP_NM")
    val orderMenuOptionGroupNm: String
) : BaseEntity()

fun OrderMenuOptionGroup.toJpaEntity() =
    OrderMenuOptionGroupJpaEntity(
        id,
        orderMenuId,
        orderMenuOptionGroupNm
    )

fun OrderMenuOptionGroupJpaEntity.toModel() =
    OrderMenuOptionGroup(
        id,
        orderMenuId,
        orderMenuOptionGroupNm
    )
