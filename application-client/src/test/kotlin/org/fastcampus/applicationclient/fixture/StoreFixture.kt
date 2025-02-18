package org.fastcampus.applicationclient.fixture

import org.fastcampus.store.entity.Store
import org.fastcampus.store.entity.StoreMenuCategory

fun createStore(
    _id: String? = "store_001",
    address: String? = "서울시 강남구 역삼동 123-45",
    border: String? = null,
    breakTime: String? = "15:00~16:00",
    category: Store.Category = Store.Category.CAFE,
    id: String? = "STORE_001",
    name: String? = "테스트 카페",
    latitude: Double? = 37.498095,
    jibunAddress: String? = "서울시 강남구 역삼동 123-45",
    longitude: Double? = 127.027610,
    ownerId: String? = "owner_123",
    tel: String? = "02-1234-5678",
    imageThumbnail: String? = "https://example.com/image_thumbnail.jpg",
    imageMain: String? = "https://example.com/image_main.jpg",
    status: Store.Status = Store.Status.OPEN,
    roadAddress: String? = "서울시 강남구 테헤란로 456",
    storeMenuCategory: List<StoreMenuCategory>? = emptyList(),
): Store {
    return Store(
        _id = _id,
        address = address,
        border = border,
        breakTime = breakTime,
        category = category,
        id = id,
        name = name,
        latitude = latitude,
        jibunAddress = jibunAddress,
        longitude = longitude,
        ownerId = ownerId,
        tel = tel,
        imageThumbnail = imageThumbnail,
        imageMain = imageMain,
        status = status,
        roadAddress = roadAddress,
        storeMenuCategory = storeMenuCategory,
        minimumOrderAmount = 15000,
    )
}
