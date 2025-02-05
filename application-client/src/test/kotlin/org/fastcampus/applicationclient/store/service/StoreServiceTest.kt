package org.fastcampus.applicationclient.store.service

import org.fastcampus.applicationclient.store.mapper.fetchDistance
import org.fastcampus.store.entity.Menu
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
import strikt.assertions.get
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue
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
    fun `getCategories should return categories`() {
        // given
        val storeId = "store-123"
        val testStore = Store(
            _id = "mongo-id-123",
            address = "서울 강남구 테헤란로 123",
            border = "050-1234-5678",
            breakTime = "15:00~17:00",
            category = Store.Category.KOREAN_CUISINE,
            id = storeId,
            name = "테스트 가게",
            latitude = 37.123456,
            jibunAddress = "서울 강남구 역삼동 123-4",
            longitude = 127.123456,
            ownerId = "owner-123",
            tel = "02-1234-5678",
            imageThumbnail = "thumbnail.jpg",
            imageMain = "main.jpg",
            status = Store.Status.OPEN,
            roadAddress = "서울 강남구 테헤란로 123",
            storeMenuCategory = listOf(
                StoreMenuCategory(
                    id = "cat-1",
                    name = "인기 메뉴",
                    storeId = storeId,
                    menu = listOf(
                        Menu(
                            id = "menu-1",
                            name = "떡볶이",
                            price = "12000",
                            desc = "매콤한 떡볶이",
                            imgUrl = "tteok.jpg",
                            isSoldOut = false,
                            isHided = false,
                            menuCategoryId = "cat-1",
                            menuOptionGroup = emptyList(),
                            order = 1L,
                        ),
                    ),
                    order = 1L,
                ),
                StoreMenuCategory(
                    id = "cat-2",
                    name = "세트 메뉴",
                    storeId = storeId,
                    menu = listOf(
                        Menu(
                            id = "menu-2",
                            name = "떡볶이 세트",
                            price = "15000",
                            desc = "떡볶이+김밥 세트",
                            imgUrl = "set.jpg",
                            isSoldOut = true,
                            isHided = false,
                            menuCategoryId = "cat-2",
                            menuOptionGroup = emptyList(),
                            order = 2L,
                        ),
                    ),
                    order = 2L,
                ),
            ),
        )

        `when`(storeRepository.findById(storeId)).thenReturn(testStore)

        // when
        val result = storeService.getCategories(storeId)

        // then
        expectThat(result) {
            hasSize(2)
            and {
                get(0).and {
                    get { categoryId }.isEqualTo("cat-1")
                    get { categoryName }.isEqualTo("인기 메뉴")
                    get { menus }.hasSize(1).and {
                        get(0).and {
                            get { id }.isEqualTo("menu-1")
                            get { name }.isEqualTo("떡볶이")
                            get { price }.isEqualTo(12000)
                            get { description }.isEqualTo("매콤한 떡볶이")
                            get { imageUrl }.isEqualTo("tteok.jpg")
                            get { soldOut }.isFalse()
                        }
                    }
                }
                get(1).and {
                    get { categoryId }.isEqualTo("cat-2")
                    get { categoryName }.isEqualTo("세트 메뉴")
                    get { menus }.hasSize(1).and {
                        get(0).and {
                            get { soldOut }.isTrue()
                        }
                    }
                }
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
        val storeId = "nonexistent_menus"
        val menuId = "nonexistent_menus"

        `when`(storeRepository.findById(storeId)).thenReturn(null)

        assertThrows<Exception>("Store id: $storeId menu id: $menuId not found") {
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
