package org.fastcampus.applicationclient.order.service

import org.fastcampus.applicationclient.order.controller.dto.request.OrderCreationRequest
import org.fastcampus.order.entity.Order
import org.fastcampus.order.entity.OrderMenu
import org.fastcampus.order.entity.OrderMenuOption
import org.fastcampus.order.entity.OrderMenuOptionGroup
import org.fastcampus.order.repository.OrderMenuOptionGroupRepository
import org.fastcampus.order.repository.OrderMenuOptionRepository
import org.fastcampus.order.repository.OrderMenuRepository
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.payment.entity.Payment
import org.fastcampus.payment.repository.PaymentRepository
import org.fastcampus.store.entity.Menu
import org.fastcampus.store.entity.MenuOption
import org.fastcampus.store.entity.MenuOptionGroup
import org.fastcampus.store.entity.Store
import org.fastcampus.store.entity.StoreMenuCategory
import org.fastcampus.store.repository.StoreRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotBlank
import kotlin.random.Random
import kotlin.random.nextLong

class OrderCreationServiceTest {
    private lateinit var orderRepository: OrderRepository
    private lateinit var storeRepository: StoreRepository
    private lateinit var paymentRepository: PaymentRepository
    private lateinit var orderMenuRepository: OrderMenuRepository
    private lateinit var orderMenuOptionGroupRepository: OrderMenuOptionGroupRepository
    private lateinit var orderMenuOptionRepository: OrderMenuOptionRepository
    private lateinit var orderService: OrderService

    @BeforeEach
    fun init() {
        orderRepository = mock(OrderRepository::class.java)
        storeRepository = mock(StoreRepository::class.java)
        paymentRepository = mock(PaymentRepository::class.java)
        orderMenuRepository = mock(OrderMenuRepository::class.java)
        orderMenuOptionGroupRepository = mock(OrderMenuOptionGroupRepository::class.java)
        orderMenuOptionRepository = mock(OrderMenuOptionRepository::class.java)
        orderService = OrderService(
            orderRepository = orderRepository,
            storeRepository = storeRepository,
            paymentRepository = paymentRepository,
            orderMenuRepository = orderMenuRepository,
            orderMenuOptionGroupRepository = orderMenuOptionGroupRepository,
            orderMenuOptionRepository = orderMenuOptionRepository,
        )
    }

    @Test
    fun `createOrder return correct response`() {
        // given
        val store = createStore()
        val storeId = store.id!!

        val storeMenus = store.storeMenuCategory!!.flatMap { it.menu!! }
        val request = createOrderCreationRequest(storeMenus)
        val menuMap = storeMenus.associateBy { it.id!! }

        val requestTotalPrice = calculateRequestMenuTotalPrice(request, menuMap)

        `when`(storeRepository.findById(storeId))
            .thenReturn(store)
        `when`(paymentRepository.save(any()))
            .thenAnswer { (it.arguments[0] as Payment).copy(id = 1) }
        `when`(orderRepository.save(any()))
            .thenAnswer { it.arguments[0] }
        `when`(orderMenuRepository.save(any()))
            .thenAnswer { (it.arguments[0] as OrderMenu).copy(id = 1) }
        `when`(orderMenuOptionGroupRepository.save(any()))
            .thenAnswer { (it.arguments[0] as OrderMenuOptionGroup).copy(id = 1) }
        `when`(orderMenuOptionRepository.save(any()))
            .thenAnswer { (it.arguments[0] as OrderMenuOption).copy(id = 1) }

        // when
        val result = orderService.createOrder(1, request)

        // then
        expectThat(result) {
            get { orderId }.isNotBlank()
            get { orderSummary }.isNotBlank()
            get { totalPrice }.isEqualTo(requestTotalPrice)
        }
    }

    private fun calculateRequestMenuTotalPrice(request: OrderCreationRequest, menuMap: Map<String, Menu>): Long {
        return request.orderMenus.sumOf { orderMenu ->
            val menu = menuMap[orderMenu.id]!!
            val menuOptionPriceMap = menu.menuOptionGroup!!.flatMap { it.getMenuOptions() }.associate { it.id to it.price }
            val orderMenuOptionIdSet = orderMenu.orderMenuOptionGroups.flatMap { it.orderMenuOptionIds }.toSet()

            val menuPrice = menu.price
            val quantity = orderMenu.quantity
            val optionPrice = orderMenu.orderMenuOptionGroups
                .flatMap { it -> it.orderMenuOptionIds.filter { orderMenuOptionIdSet.contains(it) } }
                .sumOf { menuOptionPriceMap[it]!! }
            (menuPrice!!.toLong() + optionPrice) * quantity
        }
    }

    private fun createOrderCreationRequest(storeMenus: List<Menu>): OrderCreationRequest {
        return OrderCreationRequest(
            storeId = "STORE-1",
            roadAddress = "road",
            jibunAddress = "jibun",
            detailAddress = "detail",
            orderType = Order.Type.DELIVERY,
            paymentType = Payment.Type.TOSS_PAY,
            orderMenus = storeMenus.map { storeMenu ->
                OrderCreationRequest.OrderMenu(
                    id = storeMenu.id!!,
                    quantity = Random.nextLong(1L..5L),
                    orderMenuOptionGroups = storeMenu.menuOptionGroup!!.map { menuOptionGroup ->
                        OrderCreationRequest.OrderMenu.OrderMenuOptionGroup(
                            id = menuOptionGroup.id!!,
                            orderMenuOptionIds = menuOptionGroup.getMenuOptions().map { it.id!! },
                        )
                    },
                )
            },
        )
    }

    private fun createStore(): Store {
        return Store(
            id = "STORE-1",
            _id = "_STORE-1",
            ownerId = "test owner",
            name = "test store",
            imageMain = "test1.png",
            imageThumbnail = "test2.png",
            address = "test address",
            roadAddress = "test road address",
            jibunAddress = "test jibun address",
            latitude = 37.5665,
            longitude = 126.9780,
            tel = "123-456-78",
            status = Store.Status.OPEN,
            category = Store.Category.BBQ,
            border = "500",
            breakTime = "오후 3시 ~ 오후 5시",
            storeMenuCategory = listOf(
                StoreMenuCategory(
                    id = "CATEGORY-1",
                    name = "test category 1",
                    order = 1,
                    storeId = null,
                    menu = listOf(
                        Menu(
                            id = "MENU-1",
                            name = "test menu 1",
                            price = "1000",
                            imgUrl = "test-menu-1.png",
                            desc = "TEST MENU 1",
                            isSoldOut = false,
                            isHided = false,
                            order = 1,
                            menuCategoryId = null,
                            menuOptionGroup = listOf(
                                MenuOptionGroup(
                                    id = "GROUP-1",
                                    name = "test group 1",
                                    minSel = 0,
                                    maxSel = 2,
                                    order = 1,
                                    menuOption = listOf(
                                        MenuOption(
                                            id = "OPTION-1",
                                            name = "test option 1",
                                            price = 100,
                                            isSoldOut = false,
                                            order = 1,
                                        ),
                                        MenuOption(
                                            id = "OPTION-2",
                                            name = "test option 2",
                                            price = 500,
                                            isSoldOut = false,
                                            order = 2,
                                        ),
                                    ),
                                ),
                                MenuOptionGroup(
                                    id = "GROUP-2",
                                    name = "test group 2",
                                    minSel = 2,
                                    maxSel = 2,
                                    order = 2,
                                    menuOption = listOf(
                                        MenuOption(
                                            id = "OPTION-3",
                                            name = "test option 3",
                                            price = 200,
                                            isSoldOut = false,
                                            order = 1,
                                        ),
                                        MenuOption(
                                            id = "OPTION-4",
                                            name = "test option 4",
                                            price = 600,
                                            isSoldOut = false,
                                            order = 2,
                                        ),
                                    ),
                                ),
                            ),
                        ),
                        Menu(
                            id = "MENU-2",
                            name = "test menu 2",
                            price = "2000",
                            imgUrl = "test-menu-2.png",
                            desc = "TEST MENU 2",
                            isSoldOut = false,
                            isHided = false,
                            order = 2,
                            menuCategoryId = null,
                            menuOptionGroup = listOf(
                                MenuOptionGroup(
                                    id = "GROUP-3",
                                    name = "test group 3",
                                    minSel = 0,
                                    maxSel = 3,
                                    order = 1,
                                    menuOption = listOf(
                                        MenuOption(
                                            id = "OPTION-5",
                                            name = "test option 5",
                                            price = 300,
                                            isSoldOut = false,
                                            order = 1,
                                        ),
                                        MenuOption(
                                            id = "OPTION-6",
                                            name = "test option 6",
                                            price = 700,
                                            isSoldOut = false,
                                            order = 2,
                                        ),
                                    ),
                                ),
                                MenuOptionGroup(
                                    id = "GROUP-4",
                                    name = "test group 4",
                                    minSel = 1,
                                    maxSel = 2,
                                    order = 2,
                                    menuOption = listOf(
                                        MenuOption(
                                            id = "OPTION-7",
                                            name = "test option 7",
                                            price = 400,
                                            isSoldOut = false,
                                            order = 1,
                                        ),
                                        MenuOption(
                                            id = "OPTION-8",
                                            name = "test option 8",
                                            price = 800,
                                            isSoldOut = false,
                                            order = 2,
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
                StoreMenuCategory(
                    id = "CATEGORY-2",
                    name = "test category 2",
                    storeId = null,
                    order = 2,
                    menu = listOf(),
                ),
            ),
        )
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        @Suppress("UNCHECKED_CAST")
        return null as T
    }
}
