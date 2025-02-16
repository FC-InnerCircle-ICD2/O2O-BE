package org.fastcampus.cart.document

import org.fastcampus.cart.entity.CartMenu
import org.springframework.data.mongodb.core.mapping.Field

class CartMenuDocument(
    val sequence: Long,
    val menuId: String,
    val quantity: Long,
    @Field(name = "cartMenuOptionGroups")
    val cartMenuOptionGroupDocuments: List<CartMenuOptionGroupDocument>,
)

fun CartMenuDocument.toModel(): CartMenu {
    return CartMenu(
        sequence = sequence,
        menuId = menuId,
        quantity = quantity,
        optionGroups = cartMenuOptionGroupDocuments.map { it.toModel() },
    )
}

fun CartMenu.toDocument(): CartMenuDocument {
    return CartMenuDocument(
        sequence = sequence,
        menuId = menuId,
        quantity = quantity,
        cartMenuOptionGroupDocuments = optionGroups.map { it.toDocument() },
    )
}
