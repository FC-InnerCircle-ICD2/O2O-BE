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
import org.fastcampus.payment.entity.Payment
import org.fastcampus.payment.entity.Payment.Type

@Entity
@Table(name = "PAYMENT")
class PaymentJpaEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    val type: Type,
    @Column(name = "PAYMENT_PRICE")
    val paymentPrice: Long?,
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    val status: Payment.Status,
    @Column(name = "PG_KEY")
    val pgKey: String? = null,
) : BaseEntity()

fun Payment.toJpaEntity() =
    PaymentJpaEntity(
        id,
        type,
        paymentPrice,
        status,
        pgKey,
    )

fun PaymentJpaEntity.toModel() =
    Payment(
        id,
        type,
        paymentPrice,
        status,
        pgKey,
    )
