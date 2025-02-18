package org.fastcampus.store.entity

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class MenuOptionGroup(
    val id: String? = null,
    val name: String?,
    val minSel: Int?,
    val maxSel: Int?,
    val menuOption: List<MenuOption>?,
    val order: Long?,
) {
    // 메뉴 옵션 데이터를 동적으로 생성하는 함수
    fun getMenuOptions(): List<MenuOption> {
        return menuOption ?: emptyList()
    }
}
