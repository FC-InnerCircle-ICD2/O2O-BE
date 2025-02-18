package org.fastcampus.payment.postgres.repository

import org.fastcampus.payment.entity.Payment
import org.fastcampus.payment.postgres.entity.toJpaEntity
import org.fastcampus.payment.postgres.entity.toModel
import org.fastcampus.payment.repository.PaymentRepository
import org.springframework.stereotype.Repository

@Repository
class PaymentJpaRepositoryCustom(
    private val paymentJpaRepository: PaymentJpaRepository,
) : PaymentRepository {
    override fun findById(id: Long): Payment? {
        return paymentJpaRepository.findById(id).orElse(null).toModel()
    }

    override fun save(payment: Payment): Payment {
        return paymentJpaRepository.save(payment.toJpaEntity()).toModel()
    }
}
