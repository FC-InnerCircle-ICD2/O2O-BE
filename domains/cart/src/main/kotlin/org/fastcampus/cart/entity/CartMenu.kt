package org.fastcampus.cart.entity

data class CartMenu(
    val menuId: String,
    var quantity: Long,
    val optionGroups: List<CartMenuOptionGroup>,
    var sequence: Long = 0,
    val id: String? = null,
) {
    fun matchesMenuSelection(otherCartMenu: CartMenu): Boolean {
        // 메뉴 ID 다른지 확인
        if (this.menuId != otherCartMenu.menuId) return false

        // 옵션그룹 선택 수가 다른지 확인
        if (this.optionGroups.size != otherCartMenu.optionGroups.size) return false

        // 옵션그룹 동일한지 확인
        val optionGroupMap = this.optionGroups.associateBy { it.groupId }
        val otherOptionGroupMap = otherCartMenu.optionGroups.associateBy { it.groupId }
        if ((optionGroupMap.keys - otherOptionGroupMap.keys).isNotEmpty()) return false

        // 옵션그룹의 선택 옵션까지 동일한지 확인
        return optionGroupMap.any { (optionGroupId, optionGroup) ->
            val otherOptionGroup = otherOptionGroupMap[optionGroupId]!!
            (optionGroup.optionIds.toSet() - otherOptionGroup.optionIds.toSet()).isNotEmpty()
        }.not()
    }
}
