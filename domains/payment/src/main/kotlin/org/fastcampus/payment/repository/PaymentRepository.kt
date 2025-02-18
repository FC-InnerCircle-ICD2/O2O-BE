package org.fastcampus.payment.repository

import org.fastcampus.payment.entity.Payment

interface PaymentRepository {
    fun findById(id: Long): Payment?

    fun save(payment: Payment): Payment
}
