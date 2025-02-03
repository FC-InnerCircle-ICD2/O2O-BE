package org.fastcampus.review.postgres.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["org.fastcampus.review.postgres.repository"])
class ReviewPostgresConfig
