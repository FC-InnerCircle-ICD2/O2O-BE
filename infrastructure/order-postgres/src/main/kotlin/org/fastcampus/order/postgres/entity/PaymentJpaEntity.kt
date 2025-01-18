package org.fastcampus.order.postgres.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fastcampus.common.entity.BaseEntity
import org.fastcampus.order.entity.Payment
import org.fastcampus.order.entity.Payment.Type

@Entity
@Table(name = "PAYMENT")
class PaymentJpaEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long? = null,
    val type: Type,
    val paymentPrice: Long?,
) : BaseEntity()

fun Payment.toJpaEntity() =
    PaymentJpaEntity(
        id,
        type,
        paymentPrice,
    )

fun PaymentJpaEntity.toModel() =
    Payment(
        id,
        type,
        paymentPrice,
    )
