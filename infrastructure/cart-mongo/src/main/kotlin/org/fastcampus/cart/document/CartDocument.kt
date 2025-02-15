package org.fastcampus.cart.document

import org.bson.types.ObjectId
import org.fastcampus.cart.entity.Cart
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "carts")
class CartDocument(
    val userId: Long,
    val storeId: String,
    @Field(name = "cartMenus")
    val cartMenuDocuments: List<CartMenuDocument>,
    @Id
    @Field(name = "_id")
    val id: ObjectId? = null,
)

fun CartDocument.toModel(): Cart {
    return Cart(
        userId = userId,
        storeId = storeId,
        orderMenus = cartMenuDocuments.map { it.toModel() },
        id = id.toString(),
    )
}

fun Cart.toDocument(): CartDocument {
    return CartDocument(
        userId = userId,
        storeId = storeId,
        cartMenuDocuments = orderMenus.map { it.toDocument() },
        id = id?.run { ObjectId(id) },
    )
}
