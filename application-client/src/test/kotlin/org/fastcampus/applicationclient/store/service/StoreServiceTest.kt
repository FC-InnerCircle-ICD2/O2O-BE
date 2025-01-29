package org.fastcampus.applicationclient.store.service

import org.fastcampus.applicationclient.store.mapper.fetchDistance
import org.fastcampus.store.entity.Store
import org.fastcampus.store.entity.StoreMenuCategory
import org.fastcampus.store.exception.StoreException
import org.fastcampus.store.redis.Coordinates
import org.fastcampus.store.redis.StoreRedisRepository
import org.fastcampus.store.repository.StoreRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.hasSize
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.message

@ExtendWith(MockitoExtension::class)
class StoreServiceTest {
    private lateinit var storeRepository: StoreRepository
    private lateinit var storeRedisRepository: StoreRedisRepository
    private lateinit var storeService: StoreService

    @BeforeEach
    fun setup() {
        storeRepository = mock(StoreRepository::class.java)
        storeRedisRepository = mock(StoreRedisRepository::class.java)
        storeService = StoreService(storeRepository, storeRedisRepository)
    }

    @Test
    fun `getStoreInfo should return correct store info`() {
        // given
        val storeId = "test-store-id"
        val userCoordinates = Coordinates(37.5665, 126.9780)
        val store = createTestStore(storeId)
        val storeCoordinates = Coordinates(store.latitude!!, store.longitude!!)

        `when`(storeRepository.findById(storeId)).thenReturn(store)
        `when`(storeRedisRepository.getStoreLocation(storeId)).thenReturn(storeCoordinates)
        `when`(storeRedisRepository.fetchDistance(userCoordinates, storeId)).thenReturn(3.0)

        // when
        val result = storeService.getStoreInfo(storeId, userCoordinates)

        // then
        expectThat(result) {
            get { id }.isEqualTo(storeId)
            get { name }.isEqualTo("Test Store")
            get { imageMain }.isEqualTo("test-image.jpg")
            get { address }.isEqualTo("Test Address")
            get { phone }.isEqualTo("123-456-7890")
            get { deliveryTime }.isEqualTo("25 분")
        }
    }

    @Test
    fun `getStoreInfo should throw StoreNotFoundException when store not found`() {
        // given
        val storeId = "non-existent-store"
        val userCoordinates = Coordinates(37.5665, 126.9780)

        `when`(storeRepository.findById(storeId)).thenReturn(null)

        // then
        expectThrows<StoreException.StoreNotFoundException> {
            storeService.getStoreInfo(storeId, userCoordinates)
        }.message.isEqualTo("Store not found: $storeId")
    }

    @Test
    fun `getCategories should return paginated categories`() {
        // given
        val storeId = "test-store-id"
        val store = createTestStoreWithCategories(storeId)

        `when`(storeRepository.findById(storeId)).thenReturn(store)

        // when
        val result = storeService.getCategories(storeId, 1, 1)

        // then
        expectThat(result) {
            get { content }.hasSize(1)
            get { nextCursor }.isEqualTo(2)
            get { content.first() }.and {
                get { categoryId }.isEqualTo("cat1")
                get { categoryName }.isEqualTo("Category 1")
                get { menus }.isNotNull().isEmpty()
            }
        }
    }

    @Test
    fun `calculateDeliveryDetails should return correct delivery information`() {
        // given
        val storeId = "test-store-id"
        val userCoordinates = Coordinates(37.5665, 126.9780)
        val storeCoordinates = Coordinates(37.5665, 126.9780)

        `when`(storeRedisRepository.getStoreLocation(storeId)).thenReturn(storeCoordinates)
        `when`(storeRedisRepository.fetchDistance(userCoordinates, storeId)).thenReturn(3.0)

        // when
        val result = storeService.calculateDeliveryDetails(storeId, userCoordinates)

        // then
        expectThat(result) {
            get { get("storeId") }.isEqualTo(storeId)
            get { get("distance") }.isEqualTo(3.0)
            get { get("deliveryTime") }.isEqualTo("25 분")
        }
    }

    @Test
    fun `should throw exception when store id and menu id not found`() {
        val storeId = "nonexistent_store"
        val menuId = "nonexistent_menus"

        `when`(storeRepository.findById(storeId)).thenReturn(null)

        assertThrows<IllegalArgumentException>("Store id: $storeId menu id: $menuId not found") {
            storeService.getMenusOptions(storeId, menuId)
        }
    }

    @Test
    fun `getTrendKeywords should return trend keywords in order`() {
        // given
        val keywords = mapOf(
            "마초갈비" to 5L,
            "마라공방" to 4L,
            "마우디브런치바" to 3L,
            "마당재한우국밥" to 2L,
            "마장동고기집 답십리점" to 1L,
        )
        val keywordList = listOf("마초갈비", "마라공방", "마우디브런치바", "마당재한우국밥", "마장동고기집 답십리점")

        `when`(storeRedisRepository.getTrendKeywords()).thenReturn(keywords)

        // when
        val result = storeService.getTrendKeywords()

        // then
        expectThat(result) {
            result?.trendKeywords?.forEachIndexed { index, trendKeyword ->
                get { trendKeyword.keyword }.isEqualTo(keywordList[index])
                get { trendKeyword.order }.isEqualTo(index + 1)
            }
        }
    }

    @Test
    fun `getStoreSuggestions should return paginated store suggestions`() {
        // given
        val affix = "치킨"
        val page = 1
        val size = 5
        val storeNameList = listOf(
            "림스치킨", "BHC치킨", "교촌치킨", "피자나라 치킨공주", "가마치통닭", "계동치킨",
            "훌랄라차킨", "멕시카나치킨", "찬&찬치킨", "바른치킨", "치킨플러스", "처갓집 양념치킨",
        )

        `when`(storeRedisRepository.getSuggestions(affix, page, size)).thenReturn(storeNameList)

        // when
        val result = storeService.getStoreSuggestions(affix, page, size)

        // then
        expectThat(result) {
            get { content }.hasSize(5)
            get { nextCursor }.isEqualTo(2)
        }
    }

    private fun createTestStore(storeId: String) =
        Store(
            id = storeId,
            name = "Test Store",
            imageMain = "test-image.jpg",
            address = "Test Address",
            latitude = 37.5665,
            longitude = 126.9780,
            tel = "123-456-7890",
            status = Store.Status.OPEN,
            category = Store.Category.CAFE,
            storeMenuCategory = emptyList(),
            _id = null,
            border = null,
            breakTime = null,
            jibunAddress = null,
            ownerId = null,
            imageThumbnail = null,
            roadAddress = null,
        )

    private fun createTestStoreWithCategories(storeId: String) =
        Store(
            id = storeId,
            storeMenuCategory = listOf(
                StoreMenuCategory(
                    id = "cat1",
                    name = "Category 1",
                    menu = emptyList(),
                    storeId = storeId,
                    order = 1,
                ),
                StoreMenuCategory(
                    id = "cat2",
                    name = "Category 2",
                    menu = emptyList(),
                    storeId = storeId,
                    order = 2,
                ),
            ),
            name = null,
            address = null,
            border = null,
            breakTime = null,
            category = null,
            latitude = null,
            jibunAddress = null,
            longitude = null,
            ownerId = null,
            tel = null,
            imageThumbnail = null,
            imageMain = null,
            status = Store.Status.OPEN,
            roadAddress = null,
            _id = null,
        )
}
