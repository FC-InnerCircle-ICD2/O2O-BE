package org.fastcampus.applicationclient.cart.service

import org.fastcampus.applicationclient.cart.controller.dto.request.CartMenuInsertionRequest
import org.fastcampus.applicationclient.cart.controller.dto.response.CartResponse
import org.fastcampus.applicationclient.order.service.OrderValidator
import org.fastcampus.cart.entity.Cart
import org.fastcampus.cart.entity.CartMenu
import org.fastcampus.cart.entity.CartMenuOptionGroup
import org.fastcampus.cart.exception.CartException
import org.fastcampus.cart.repository.CartRepository
import org.fastcampus.store.entity.Menu
import org.fastcampus.store.repository.StoreRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CartMenuInsertionService(
    private val cartRepository: CartRepository,
    private val storeRepository: StoreRepository,
) {
    fun insertCartMenu(userId: Long, cartMenuInsertionRequest: CartMenuInsertionRequest) {
        // TODO - '따닥' 고려 레디스로 글로벌 락 사용?. 일단 그냥 만들자

        // 유저 ID로 장바구니 검색
        val cart = cartRepository.findByUserId(userId)?.apply {
            // 장바구니가 있다면 가게 ID 일치하는지  검사
            this.checkStoreIdAndThrow(cartMenuInsertionRequest.storeId)
        } ?: Cart(userId, cartMenuInsertionRequest.storeId, emptyList())

        // 가게 ID로 스토어 정보 조회
        val store = storeRepository.findById(cartMenuInsertionRequest.storeId)
            ?: throw CartException.StoreNotFound(cartMenuInsertionRequest.storeId)

        // 주문쪽의 주문메뉴 검사 수행
        OrderValidator.checkOrderMenus(store, listOf(cartMenuInsertionRequest.orderMenu.toRequestedOrderMenu()))

        // 기존 카트메뉴 정보 없으면 저장 후 종료
        if (cart.orderMenus.isEmpty()) {
            cartRepository.save(cartMenuInsertionRequest.toCart(userId))
            return
        }

        // 기존 동일 옵션선택 메뉴 존재시 해당메뉴 수량만 증가, 미존재시 추가됨
        val updatedCart = cart.createCartWithAddedMenu(cartMenuInsertionRequest.orderMenu.toCartMenu())

        cartRepository.save(updatedCart)
    }

    fun getCart(userId: Long): CartResponse {
        // 장바구니 못찾으면 정상응답, 빈 값 반환
        val cart = cartRepository.findByUserId(userId) ?: return CartResponse("", emptyList())

        // 가게 ID로 스토어 정보 조회
        val store = storeRepository.findById(cart.storeId)
            ?: throw CartException.StoreNotFound(cart.storeId)

        // 가게의 메뉴정보 <MenuID, Menu>
        val menuInfoById = store.storeMenuCategory?.flatMap { it.menu ?: emptyList() }?.associateBy { it.id } ?: emptyMap()

        // 장바구니에 담긴 메뉴의 정보 조립
        val cartMenus = cart.orderMenus.map { cartMenu ->
            val menuInfo = menuInfoById[cartMenu.menuId]

            // 메뉴 옵션그룹 정보 <MenuOptionGroupId, MenuOptionGroup>
            val menuOptionGroupInfoById = menuInfo?.menuOptionGroup?.associateBy { it.id } ?: emptyMap()
            CartResponse.CartMenu(
                cartId = cartMenu.sequence,
                menuId = cartMenu.menuId,
                name = menuInfo?.name ?: "알 수 없음",
                imageUrl = menuInfo?.imgUrl ?: "",
                totalPrice = menuInfo?.calculateTotalPriceOfCartMenu(cartMenu) ?: 0,
                quantity = cartMenu.quantity,
                orderMenuOptionGroups = cartMenu.optionGroups.map { cartOptionGroup ->
                    val menuOptionGroupInfo = menuOptionGroupInfoById[cartOptionGroup.groupId]

                    // 메뉴 옵션 정보 <MenuOptionId, MenuOption>
                    val menuOptionInfoById = menuOptionGroupInfo?.menuOption?.associateBy { it.id } ?: emptyMap()
                    CartResponse.CartMenu.CartMenuOptionGroup(
                        id = cartOptionGroup.groupId,
                        name = menuOptionGroupInfo?.name ?: "알 수 없음",
                        orderMenuOptionIds = cartOptionGroup.optionIds.map { optionId ->
                            val menuOptionInfo = menuOptionInfoById[optionId]

                            CartResponse.CartMenu.CartMenuOptionGroup.CartMenuOption(
                                id = optionId,
                                name = menuOptionInfo?.name ?: "알 수 없음",
                            )
                        },
                    )
                },
            )
        }

        return CartResponse(
            storeId = cart.storeId,
            orderMenus = cartMenus,
        )
    }

    /**
     * 주문메뉴 검사용 DTO 변환
     *
     * @return 주문메뉴 검사용 RequestedOrderMenu
     */
    private fun CartMenuInsertionRequest.OrderMenu.toRequestedOrderMenu(): OrderValidator.Companion.RequestedOrderMenu {
        return OrderValidator.Companion.RequestedOrderMenu(
            id = this.menuId,
            requestedOrderMenuOptionGroups = this.orderMenuOptionGroups.map { it.toRequestedOrderMenuOptionGroup() },
        )
    }

    /**
     * 주문메뉴 검사용 DTO 변환
     *
     * @return RequestedOrderMenuOptionGroup
     */
    private fun CartMenuInsertionRequest.OrderMenu.OrderMenuOptionGroup.toRequestedOrderMenuOptionGroup():
        OrderValidator.Companion.RequestedOrderMenu.RequestedOrderMenuOptionGroup {
        return OrderValidator.Companion.RequestedOrderMenu.RequestedOrderMenuOptionGroup(
            id = this.id,
            requestedOrderMenuOptions = this.orderMenuOptionIds.toList(),
        )
    }

    /**
     * 카트추가 요청 DTO 를 도메인 객체 [Cart]로 변환한다.
     *
     * 카트추가 요청메뉴 1개가 포함되며, 메뉴의 추가는 [Cart.createCartWithAddedMenu] 을 사용한다.
     *
     * @param userId    주문 유저의 ID
     * @return 카트추가 요청 메뉴 1개가 포함된 [Cart] 도메인 객체
     */
    private fun CartMenuInsertionRequest.toCart(userId: Long): Cart {
        return Cart(
            userId = userId,
            storeId = this.storeId,
            orderMenus = mutableListOf(this.orderMenu.toCartMenu()),
        )
    }

    /**
     * 카트추가 요청 DTO 도메인 객체 [CartMenu] 변환용
     *
     * @return [CartMenuOptionGroup] 포함 된 [CartMenu] 도메인 객체
     */
    private fun CartMenuInsertionRequest.OrderMenu.toCartMenu(): CartMenu {
        return CartMenu(
            menuId = this.menuId,
            quantity = this.quantity,
            optionGroups = this.orderMenuOptionGroups.map { it.toCartMenuOptionGroup() },
        )
    }

    /**
     * 카트추가 요청 DTO 도메인 객체 [CartMenuOptionGroup] 변환용
     *
     * @return 옵션 ID 목록을 가진 [CartMenuOptionGroup] 도메인 객체
     */
    private fun CartMenuInsertionRequest.OrderMenu.OrderMenuOptionGroup.toCartMenuOptionGroup(): CartMenuOptionGroup {
        return CartMenuOptionGroup(
            groupId = this.id,
            optionIds = this.orderMenuOptionIds.toList(),
        )
    }

    private fun Menu.calculateTotalPriceOfCartMenu(cartMenu: CartMenu): Long {
        if (this.id != cartMenu.menuId) {
            logger.info("메뉴 ID [${this.id}] 와 장바구니 메뉴 ID [${cartMenu.menuId}] 불일치")
            return 0
        }

        val cartOptionGroupById = cartMenu.optionGroups.associateBy { it.groupId }
        val menuOptionGroupById = this.menuOptionGroup?.associateBy { it.id } ?: emptyMap()

        // 옵션 가격
        val optionPrice = cartOptionGroupById.map { (groupId, cartMenuOptionGroup) ->
            val menuOptionGroupInfo = menuOptionGroupById[groupId] ?: return@map 0

            val menuOptionInfoById = menuOptionGroupInfo.menuOption?.associateBy { it.id } ?: emptyMap()

            cartMenuOptionGroup.optionIds.sumOf { optionId ->
                menuOptionInfoById[optionId]?.price ?: 0
            }
        }.sum()

        // (옵션 가격 + 메뉴 가격) * 수량
        return (optionPrice + this.getLongTypePrice()) * cartMenu.quantity
    }

    private fun Menu.getLongTypePrice(): Long {
        // 현재 테스트데이터 형식 "4,000" / "5000" 처럼 문자열임
        val tmpPrice = this.price?.replace(",", "")
        return tmpPrice
            .runCatching { tmpPrice?.toLong() }
            .getOrNull() ?: 0
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CartMenuInsertionService::class.java)
    }
}
