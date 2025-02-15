package org.fastcampus.cart.entity

import org.fastcampus.cart.exception.CartException

data class Cart(
    val userId: Long,
    val storeId: String,
    val orderMenus: List<CartMenu>,
    val id: String? = null,
) {
    fun checkStoreIdAndThrow(storeId: String) {
        if (this.storeId != storeId) {
            throw CartException.StoreNotMatches(storeId)
        }
    }

    /**
     * 카트에 메뉴를 추가한다.
     *
     * [CartMenu.sequence] 값은 입력된 객체의 값은 무시되며, 자동으로 순서번호를 저장한다.
     *
     * @param newCartMenu
     * @return
     */
    fun createCartWithAddedMenu(newCartMenu: CartMenu): Cart {
        // 똑같은 선택 메뉴가 존재하는지 확인
        val matchesCartMenu = this.orderMenus.find { it.matchesMenuSelection(newCartMenu) }

        // 똑같은 선택 메뉴가 존재한다면 수량을 늘린다.
        return matchesCartMenu?.run {
            this.quantity += newCartMenu.quantity
            this@Cart
        } ?: this.copy(
            // 없다면 추가한다.
            orderMenus = orderMenus.mapTo(mutableListOf()) { cartMenu ->
                cartMenu.id
                cartMenu.copy(
                    optionGroups = cartMenu.optionGroups.map { optionGroup ->
                        optionGroup.copy(
                            optionIds = optionGroup.optionIds.toList(),
                        )
                    },
                )
            }.apply {
                add(
                    newCartMenu.copy(
                        sequence = getNextCartSequence(),
                    ),
                )
            },
        )
    }

    /**
     * 나의 장바구니 메뉴순서 다음 ID를 얻는다.
     *
     * @return 장바구니 메뉴시퀀스 max + 1 값
     */
    private fun getNextCartSequence(): Long {
        return try {
            orderMenus.maxOf { it.sequence } + 1L
        } catch (e: NoSuchElementException) {
            0
        }
    }
}
