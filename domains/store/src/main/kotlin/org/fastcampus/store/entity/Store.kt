package org.fastcampus.store.entity

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class Store(
    val _id: String? = null,
    val id: String?,
    val name: String?,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val border: Int?,
    val ownerId: String?,
    val tel: String?,
    val imageThumbnail: String?,
    val imageMain: String?,
    val status: Status,
    val breakTime: String,
    val roadAddress: String?,
    val jibunAddress: String?,
    val category: Category?,
    val storeMenuCategory: List<StoreMenuCategory>?,
) {
    enum class Status(
        val code: String,
        val desc: String,
    ) {
        OPEN("S1", "영업"),
        CLOSE("S2", "종료"),
    }

    enum class Category(
        val code: String,
        val desc: String,
    ) {
        CAFE("C1", "카페/디저트"),
        CHICKEN("C2", "치킨"),
        KOREAN_CUISINE("C3", "한식"),
        SNACK_FOOD("C4", "분식"),
        PIZZA_WESTERN("C5", "피자/양식"),
        BURGER("C6", "버거"),
        JAPANESE_CUISINE("C7", "일식/돈까스"),
        KOREAN_STEW("C8", "찜/탕"),
        SUSHI("C9", "회/초밥"),
        PORK_DISHES("C10", "족발/보쌈"),
        BBQ("C11", "고기/구이"),
        SANDWICH("C12", "샌드위치"),
        SALAD("C13", "샐러드"),
        LUNCH_BOX("C14", "도시락/죽"),
        ASIAN_CUISINE("C15", "아시안"),
        CHINESE_CUISINE("C16", "중식"),
        ETC("C17", "기타"),
    }
}
