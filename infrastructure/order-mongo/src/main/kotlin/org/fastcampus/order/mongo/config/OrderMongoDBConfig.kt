package org.fastcampus.order.mongo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["org.fastcampus.order.mongo.repository"])
class OrderMongoDBConfig {
    @Bean
    fun orderMongoTemplate(mongoDbFactory: MongoDatabaseFactory): MongoTemplate {
        return MongoTemplate(mongoDbFactory)
    }
}
