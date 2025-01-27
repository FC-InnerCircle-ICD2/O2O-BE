package org.fastcampus.payment.repository

import org.fastcampus.payment.entity.Refund

interface RefundRepository {
    fun findById(id: Long): Refund

    fun save(refund: Refund): Refund

    fun findAllByStatuses(statuses: List<Refund.Status>): List<Refund>
}
