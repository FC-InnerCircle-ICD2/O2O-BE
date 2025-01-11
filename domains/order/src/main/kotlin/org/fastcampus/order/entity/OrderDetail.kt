package org.fastcampus.order.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * Created by brinst07 on 25. 1. 11..
 */
@Entity
@Table(name = "주문내역")
class OrderDetail(
    @Id
    @Column(name = "ID")
    val id: String,

    @Column(name = "ORDER_ID")
    val orderId: String?,

    @Column(name = "PRICE")
    val price: String?,

    @Column(name = "PRODUCT_ID")
    val productId: String?,

    @Column(name = "PRODUCT_NAME")
    val productName: String?,

    @Column(name = "PRODUCT_QUANTITY")
    val productQuantity: String?,

    @Column(name = "PRODUCT_PRICE")
    val productPrice: String?
)
