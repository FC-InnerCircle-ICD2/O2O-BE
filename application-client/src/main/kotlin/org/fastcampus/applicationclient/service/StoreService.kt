package org.fastcampus.applicationclient.service

import org.fastcampus.store.entity.Store
import org.fastcampus.store.repository.StoreRepository
import org.springframework.stereotype.Service

/**
 * Created by brinst07 on 25. 1. 11..
 */
@Service
class StoreService(
    private val storeRepository: StoreRepository,
) {
    fun getStoresByCategory(category: String): List<Store> {
        return storeRepository.findByCategory(category)
    }
}
