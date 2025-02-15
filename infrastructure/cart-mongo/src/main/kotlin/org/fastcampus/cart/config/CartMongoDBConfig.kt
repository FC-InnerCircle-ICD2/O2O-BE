package org.fastcampus.cart.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["org.fastcampus.cart.repository"])
class CartMongoDBConfig
