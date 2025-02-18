package org.fastcampus.applicationclient.store.scheduler

import org.fastcampus.store.redis.StoreRedisRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class StoreRedisScheduler(
    private val storeRedisRepository: StoreRedisRepository,
) {
    @Scheduled(fixedRate = 1000 * 60 * 60 * 1)
    fun removeOldData() {
        storeRedisRepository.removeOldData()
    }
}
