package org.fastcampus.payment.postgres.repository

import org.fastcampus.payment.entity.Refund
import org.fastcampus.payment.exception.RefundException
import org.fastcampus.payment.postgres.entity.toJpaEntity
import org.fastcampus.payment.postgres.entity.toModel
import org.fastcampus.payment.repository.RefundRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class RefundRepositoryCustom(
    private val refundJpaRepository: RefundJpaRepository,
) : RefundRepository {
    override fun findById(id: Long): Refund {
        return refundJpaRepository.findByIdOrNull(id)?.toModel() ?: throw RefundException.RefundNotFound(id)
    }

    override fun save(refund: Refund): Refund {
        return refundJpaRepository.save(refund.toJpaEntity()).toModel()
    }

    override fun findAllByStatuses(statuses: List<Refund.Status>): List<Refund> {
        return refundJpaRepository.findAllByStatusIn(statuses).map { it.toModel() }
    }
}
