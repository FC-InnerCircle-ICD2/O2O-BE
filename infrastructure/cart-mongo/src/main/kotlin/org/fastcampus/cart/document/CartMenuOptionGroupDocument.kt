package org.fastcampus.cart.document

import org.fastcampus.cart.entity.CartMenuOptionGroup

class CartMenuOptionGroupDocument(
    val groupId: String,
    val optionIds: List<String>,
)

fun CartMenuOptionGroupDocument.toModel(): CartMenuOptionGroup {
    return CartMenuOptionGroup(
        groupId = groupId,
        optionIds = optionIds,
    )
}

fun CartMenuOptionGroup.toDocument(): CartMenuOptionGroupDocument {
    return CartMenuOptionGroupDocument(
        groupId = groupId,
        optionIds = optionIds,
    )
}
