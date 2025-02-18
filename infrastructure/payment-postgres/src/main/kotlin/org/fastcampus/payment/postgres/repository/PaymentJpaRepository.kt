package org.fastcampus.payment.postgres.repository

import org.fastcampus.payment.postgres.entity.PaymentJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentJpaRepository : JpaRepository<PaymentJpaEntity, Long>
