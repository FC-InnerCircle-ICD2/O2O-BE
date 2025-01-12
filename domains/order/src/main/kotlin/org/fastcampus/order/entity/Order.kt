package org.fastcampus.order.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fastcampus.common.entity.BaseEntity

/**
 * Created by brinst07 on 25. 1. 11..
 */
@Entity
@Table(name = "TB_ORDER")
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    val id: Long? = null,
    @Column(name = "STORE_ID")
    val storeId: String?,
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
