package org.fastcampus.cart.repository

import org.fastcampus.cart.document.toDocument
import org.fastcampus.cart.document.toModel
import org.fastcampus.cart.entity.Cart
import org.springframework.stereotype.Component

@Component
class CartMongoRepositoryCustom(
    private val cartMongoRepository: CartMongoRepository,
) : CartRepository {
    override fun findByUserId(userId: Long): Cart? {
        return cartMongoRepository.findOneByUserId(userId)?.toModel()
    }

    override fun save(cart: Cart): Cart {
        return cartMongoRepository.save(cart.toDocument()).toModel()
    }
}
