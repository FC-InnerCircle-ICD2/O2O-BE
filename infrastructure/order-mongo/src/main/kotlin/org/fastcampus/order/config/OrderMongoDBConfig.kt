package org.fastcampus.order.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["org.fastcampus.order.repository"])
class OrderMongoDBConfig
