package org.fastcampus.order.postgres.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fastcampus.order.entity.OrderOption
import org.fastcampus.order.entity.OrderOptionGroup

@Entity
@Table(name = "TB_ORDER_OPTION_GROUP")
class OrderOptionGroupJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    val id: Long? = null,
    @Column(name = "ORDER_DETAIL_ID")
    val orderDetailId: Long?,
    @Column(name = "PRODUCT_OPTION_GROUP_NM")
    val productOptionGroupNm: String?
)

fun OrderOptionGroup.toJpaEntity() =
    OrderOptionGroupJpaEntity(
        id,
        orderDetailId,
        productOptionGroupNm
    )

fun OrderOptionGroupJpaEntity.toModel() =
    OrderOptionGroup(
        id,
        orderDetailId,
        productOptionGroupNm
    )
