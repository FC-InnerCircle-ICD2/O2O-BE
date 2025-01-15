package org.fastcampus.store.mongo.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["org.fastcampus.store.mongo.repository"])
class MongoDBConfig
