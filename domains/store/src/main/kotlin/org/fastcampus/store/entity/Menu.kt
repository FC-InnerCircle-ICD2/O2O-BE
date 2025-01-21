package org.fastcampus.store.entity

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class Menu(
    val id: String? = null,
    val name: String?,
    val price: String?,
    val desc: String?,
    val imgUrl: String?,
    val isSoldOut: Boolean,
    val isHided: Boolean,
    val menuCategoryId: String?,
    val menuOptionGroup: List<MenuOptionGroup>?,
    val order: Long,
) {
    // 메뉴 옵션 그룹 데이터를 동적으로 생성하는 함수
    fun getMenuOptionGroups(): List<MenuOptionGroup> {
        return menuOptionGroup ?: emptyList()
    }
}
