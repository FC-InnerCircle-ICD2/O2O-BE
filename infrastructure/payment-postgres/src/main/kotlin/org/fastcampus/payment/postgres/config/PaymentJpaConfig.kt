package org.fastcampus.order.postgres.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["org.fastcampus.payment.postgres.repository"])
class PaymentJpaConfig
