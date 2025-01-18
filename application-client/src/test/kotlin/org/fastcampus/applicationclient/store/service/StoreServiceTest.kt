package org.fastcampus.applicationclient.store.service

import org.fastcampus.store.entity.Menu
import org.fastcampus.store.entity.Store
import org.fastcampus.store.entity.StoreMenuCategory
import org.fastcampus.store.redis.Coordinates
import org.fastcampus.store.redis.StoreRedisRepository
import org.fastcampus.store.repository.StoreRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class StoreServiceTest {
    private val storeRepository: StoreRepository = mock(StoreRepository::class.java)
    private val storeRedisRepository: StoreRedisRepository = mock(StoreRedisRepository::class.java)
    private val storeService = StoreService(storeRepository, storeRedisRepository)

    @Test
    fun `should throw exception when store not found`() {
        // Given
        val storeId = "nonexistent_store"

        `when`(storeRepository.findById(storeId)).thenReturn(null)

        // When & Then
        assertThrows<IllegalArgumentException>("Store not found: $storeId") {
            storeService.getStoreDetails(storeId, 0, 10, Coordinates(37.5665, 126.978))
        }
    }

    private fun createMockStore(storeId: String): Store {
        return Store(
            _id = storeId,
            id = "test",
            name = "Mock Store",
            address = "test address",
            imageMain = "https://example.com/image.png",
            latitude = 37.5665,
            longitude = 126.978,
            storeMenuCategory = listOf(
                StoreMenuCategory(
                    id = "test",
                    name = "mockMenuCategory",
                    storeId = storeId,
                    menu = listOf(
                        Menu(id = "menu1", name = "Mock Menu 1", price = "10000원", desc = "Delicious", imgUrl = "", isSoldOut = false, isHided = false, menuCategoryId = "menuCategoryId", menuOptionGroup = null, order = 1),
                        Menu(id = "menu2", name = "Mock Menu 2", price = "12000원", desc = "Tasty", imgUrl = "", isSoldOut = false, isHided = false, menuCategoryId = "menuCategoryId", menuOptionGroup = null, order = 2),
                    ),
                    order = 1,
                ),
            ),
            imageThumbnail = "tst",
            status = Store.Status.OPEN,
            breakTime = "test",
            roadAddress = "test",
            jibunAddress = "test",
            ownerId = "test",
            tel = "test",
            category = Store.Category.CHINESE_CUISINE,
            border = 6000, // Free delivery threshold
        )
    }
}
