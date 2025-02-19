package org.fastcampus.favorite.postgres.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["org.fastcampus.favorite.postgres.repository"])
class FavoriteJpaConfig
