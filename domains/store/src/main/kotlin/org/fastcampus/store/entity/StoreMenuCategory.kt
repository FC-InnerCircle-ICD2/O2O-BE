package org.fastcampus.store.entity

data class StoreMenuCategory(
    val id: String? = null,
    val name: String?,
    val storeId: String?,
    val menu: List<Menu>?,
    val order: Long,
) {
    // 메뉴 데이터를 동적으로 생성하는 함수
    fun getMenus(): List<Menu> {
        return menu ?: emptyList()
    }
}
