package org.fastcampus.store.mongo.repository

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * Created by brinst07 on 25. 1. 16.
 */
@SpringBootTest
class StoreMongoRepositoryTests(
    private val storeRepo: StoreMongoRepository
) {

    @Test
    fun `dmdkr` () {
        val findByCategory = storeRepo.findByCategory("CAFE")
        println(findByCategory)
    }
}
