package org.fastcampus.payment.postgres.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fastcampus.common.entity.BaseEntity
import org.fastcampus.payment.entity.Refund

@Entity
@Table(name = "REFUNDS")
class RefundJpaEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    val status: Refund.Status,
    @Column(name = "ORDER_ID")
    val orderId: String,
    @Column(name = "ORDER_PRICE")
    val orderPrice: Long,
    @Column(name = "PAYMENT_ID")
    val paymentId: Long,
) : BaseEntity()

fun Refund.toJpaEntity() =
    RefundJpaEntity(
        id,
        status,
        orderId,
        orderPrice,
        paymentId,
    )

fun RefundJpaEntity.toModel() =
    Refund(
        id,
        status,
        orderId,
        orderPrice,
        paymentId,
    )
