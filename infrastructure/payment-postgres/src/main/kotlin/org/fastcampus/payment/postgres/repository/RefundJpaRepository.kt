package org.fastcampus.payment.postgres.repository

import org.fastcampus.payment.entity.Refund
import org.fastcampus.payment.postgres.entity.RefundJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RefundJpaRepository : JpaRepository<RefundJpaEntity, Long> {
    fun findAllByStatusIn(string: List<Refund.Status>): List<RefundJpaEntity>
}
