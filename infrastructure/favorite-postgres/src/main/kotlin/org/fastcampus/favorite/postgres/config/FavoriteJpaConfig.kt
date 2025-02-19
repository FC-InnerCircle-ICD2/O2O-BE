package org.fastcampus.favorite.postgres.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["org.fastcampus.favorite.postgres.repository"])
@EntityScan(basePackages = ["org.fastcampus.favorite.postgres.entity"])
class FavoriteJpaConfig
