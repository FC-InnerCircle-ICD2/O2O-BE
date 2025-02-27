package org.fastcampus.order.repository

interface OrderLockManager {
    fun <R> lock(orderId: String, function: () -> R): R
}
