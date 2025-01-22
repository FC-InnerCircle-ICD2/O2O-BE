package org.fastcampus.store.repository

import org.fastcampus.store.entity.Store

/**
 * Created by brinst07 on 25. 1. 11..
 */

interface StoreRepository {
    fun findByCategory(category: String): List<Store>

    fun findById(storeId: String): Store?

    fun findByNameContaining(name: String): List<String>?
}
