package org.fastcampus.cart.repository

import org.fastcampus.cart.entity.Cart

interface CartRepository {
    fun findByUserId(userId: Long): Cart?

    fun save(cart: Cart): Cart

    fun removeByUserId(userId: Long)
}
