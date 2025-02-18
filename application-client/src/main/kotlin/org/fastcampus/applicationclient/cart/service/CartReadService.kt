package org.fastcampus.applicationclient.cart.service

import org.fastcampus.applicationclient.cart.controller.dto.response.CartResponse
import org.fastcampus.cart.entity.CartMenu
import org.fastcampus.cart.exception.CartException
import org.fastcampus.cart.repository.CartRepository
import org.fastcampus.store.entity.Menu
import org.fastcampus.store.repository.StoreRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CartReadService(
    private val cartRepository: CartRepository,
    private val storeRepository: StoreRepository,
) {
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
        private val logger = LoggerFactory.getLogger(CartReadService::class.java)
    }
}
