package org.fastcampus.order.postgres.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fastcampus.common.entity.BaseEntity

/**
 * Created by brinst07 on 25. 1. 11..
 */
@Entity
@Table(name = "TB_ORDER_OPTION")
class OrderOptionJpaEntity(
    @Id
    @Column(name = "ID")
    val id: String,
    @Column(name = "PRODUCT_OPTION_ID")
    val productOptionId: String?,
    @Column(name = "PRODUCT_OPTION_NAME")
    val productOptionName: String?,
    @Column(name = "PRODUCT_OPTION_PRICE")
    val productOptionPrice: String?,
) : BaseEntity()
