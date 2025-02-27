package org.fastcampus.order.postgres.projections

/**
 * Created by brinst07 on 25. 2. 27.
 */
interface StoreOrderCountProjection {
    fun getStoreId(): String?
    fun getOrderCount(): Long
}
