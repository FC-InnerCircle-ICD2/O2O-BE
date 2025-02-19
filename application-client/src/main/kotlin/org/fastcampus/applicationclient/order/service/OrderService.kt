package org.fastcampus.applicationclient.order.service

import org.fastcampus.applicationclient.aop.OrderMetered
import org.fastcampus.applicationclient.order.controller.dto.request.OrderCreationRequest
import org.fastcampus.applicationclient.order.controller.dto.response.OrderCreationResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderDetailResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderMenuOptionGroupResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderMenuOptionResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderMenuResponse
import org.fastcampus.applicationclient.order.controller.dto.response.OrderResponse
import org.fastcampus.common.dto.CursorDTO
import org.fastcampus.member.repository.MemberRepository
import org.fastcampus.order.entity.Order
import org.fastcampus.order.entity.OrderMenu
import org.fastcampus.order.entity.OrderMenuOption
import org.fastcampus.order.entity.OrderMenuOptionGroup
import org.fastcampus.order.exception.OrderException
import org.fastcampus.order.repository.OrderMenuOptionGroupRepository
import org.fastcampus.order.repository.OrderMenuOptionRepository
import org.fastcampus.order.repository.OrderMenuRepository
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.payment.entity.Payment
import org.fastcampus.payment.repository.PaymentRepository
import org.fastcampus.store.entity.Menu
import org.fastcampus.store.entity.MenuOption
import org.fastcampus.store.repository.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class OrderService(
    private val memberRepository: MemberRepository,
    private val orderRepository: OrderRepository,
    private val storeRepository: StoreRepository,
    private val paymentRepository: PaymentRepository,
    private val orderMenuRepository: OrderMenuRepository,
    private val orderMenuOptionGroupRepository: OrderMenuOptionGroupRepository,
    private val orderMenuOptionRepository: OrderMenuOptionRepository,
) {
    @Transactional(readOnly = true)
    @OrderMetered
    fun getOrders(userId: Long, keyword: String, page: Int, size: Int): CursorDTO<OrderResponse> {
        val orders = orderRepository.findByUserIdExcludingWaitStatus(userId, keyword, page, size)
        return CursorDTO(
            content = orders.content.map { order ->
                val store = storeRepository.findById(requireNotNull(order.storeId))
                OrderResponse(
                    storeId = order.storeId,
                    storeName = store?.name,
                    imageThumbnail = store?.imageThumbnail,
                    orderId = order.id,
                    status = mapOf("code" to order.status.code, "desc" to order.status.desc),
                    orderTime = order.orderTime,
                    orderSummary = order.orderSummary,
                    deliveryCompleteTime = order.deliveryCompleteTime,
                    paymentPrice = order.paymentPrice,
                )
            },
            nextCursor = orders.nextCursor?.plus(1),
        )
    }

    @Transactional(readOnly = true)
    @OrderMetered
    fun getOrder(orderId: String): OrderDetailResponse {
        val order = requireNotNull(orderRepository.findById(orderId))
        val payment = requireNotNull(paymentRepository.findById(order.paymentId))
        val orderMenus = orderMenuRepository.findByOrderId(order.id)
        val storeName = storeRepository.findById(storeId = requireNotNull(order.storeId))?.name
        return OrderDetailResponse(
            orderId = order.id,
            storeName = storeName ?: "",
            status = mapOf("code" to order.status.code, "desc" to order.status.desc),
            orderTime = order.orderTime,
            isDeleted = order.isDeleted,
            tel = order.tel,
            roadAddress = order.roadAddress,
            jibunAddress = order.jibunAddress,
            detailAddress = order.detailAddress,
            excludingSpoonAndFork = order.excludingSpoonAndFork,
            requestToRider = order.requestToRider,
            orderPrice = order.orderPrice,
            deliveryPrice = order.deliveryPrice,
            deliveryCompleteTime = order.deliveryCompleteTime,
            paymentPrice = order.paymentPrice,
            paymentId = order.paymentId,
            paymentType = mapOf("code" to payment.type.code, "desc" to payment.type.desc),
            type = mapOf("code" to order.type.code, "desc" to order.type.desc),
            orderMenus = orderMenus.map { orderMenu ->
                val orderMenuOptionGroups = orderMenuOptionGroupRepository.findByOrderMenuId(requireNotNull(orderMenu.id))
                OrderMenuResponse(
                    id = orderMenu.id,
                    menuId = orderMenu.menuId,
                    menuName = orderMenu.menuName,
                    menuQuantity = orderMenu.menuQuantity,
                    menuPrice = orderMenu.menuPrice,
                    totalPrice = orderMenu.totalPrice,
                    orderMenuOptionGroups = orderMenuOptionGroups.map { orderMenuOptionGroup ->
                        val orderMenuOptions = orderMenuOptionRepository
                            .findByOrderMenuOptionGroupId(requireNotNull(orderMenuOptionGroup.id))
                        OrderMenuOptionGroupResponse(
                            id = orderMenuOptionGroup.id,
                            orderMenuId = orderMenuOptionGroup.orderMenuId,
                            orderMenuOptionGroupName = orderMenuOptionGroup.orderMenuOptionGroupName,
                            orderMenuOptions = orderMenuOptions.map { orderMenu ->
                                OrderMenuOptionResponse(
                                    id = orderMenu.id,
                                    orderMenuOptionGroupId = orderMenu.orderMenuOptionGroupId,
                                    menuOptionName = orderMenu.menuOptionName,
                                    menuOptionPrice = orderMenu.menuOptionPrice,
                                )
                            },
                        )
                    },
                )
            },
        )
    }

    @Transactional
    @OrderMetered
    fun createOrder(userId: Long, orderCreationRequest: OrderCreationRequest): OrderCreationResponse {
        // 사용자 정보 조회
        val loginMember = memberRepository.findById(userId)

        // 스토어 검색
        val storeEntity = (storeRepository.findById(orderCreationRequest.storeId))
            ?: throw OrderException.StoreNotFound(orderCreationRequest.storeId)

        // 주문내역 검사, 메뉴정보 반환받기
        val targetMenuEntities = OrderValidator.checkOrderCreation(storeEntity, orderCreationRequest)

        // 주문 메뉴는 여러개 나누어 들어올 수 있다. (동일메뉴 + 다른옵션)
        // 여러개의 주문메뉴, 주문메뉴옵션그룹, 주문메뉴옵션을 생성한다.
        val tempOrderMenus = createTempOrderMenus(orderCreationRequest, targetMenuEntities)

        // 메뉴 기본가격 전체 합
        val sumOfOrderMenuPrice = tempOrderMenus.sumOf { it.menuPrice * it.menuQuantity }
        // 옵션 가격 전체 합
        val sumOfOptionPrice = tempOrderMenus.sumOf { it.calculateOptionTotalPricePerMenuUnit() * it.menuQuantity }

        // 전체 주문금액
        val totalPrice = sumOfOrderMenuPrice + sumOfOptionPrice
        if (totalPrice < storeEntity.minimumOrderAmount) {
            throw OrderException.MinimumOrderAmountNotSatisfied()
        }

        // 결제 생성, 저장 - TODO 결제에 orderId 가 있어야 할 것 같다.
        val savedPayment = paymentRepository.save(
            Payment(
                type = orderCreationRequest.paymentType,
                paymentPrice = totalPrice, // 메뉴가격 + 옵션가격
            ),
        )

        val time = LocalDateTime.now()
        val today = time.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val uuid8charsInFront = UUID.randomUUID().toString().uppercase().substring(0..7)
        // 주문 생성, 저장
        val menuSize = tempOrderMenus.size
        val summary = if (menuSize == 1) tempOrderMenus[0].menuName else "${tempOrderMenus[0].menuName} 외 ${menuSize - 1}개"
        val savedOrder = orderRepository.save(
            Order(
                id = "$today-$uuid8charsInFront",
                storeId = storeEntity.id,
                storeName = "",
                storeImageThumbnail = "",
                userId = userId,
                roadAddress = orderCreationRequest.roadAddress,
                jibunAddress = orderCreationRequest.jibunAddress,
                detailAddress = orderCreationRequest.detailAddress,
                excludingSpoonAndFork = orderCreationRequest.excludingSpoonAndFork ?: true,
                requestToRider = orderCreationRequest.requestToRider,
                tel = loginMember.phone,
                status = Order.Status.WAIT,
                orderTime = time,
                orderSummary = summary,
                type = orderCreationRequest.orderType,
                paymentId = savedPayment.id!!,
                isDeleted = false,
                deliveryCompleteTime = null,
                deliveryPrice = 0L,
                orderPrice = totalPrice,
                paymentPrice = totalPrice,
            ),
        )
        // 주문 메뉴, 옵션그룹, 옵션 저장
        saveOrderSubEntities(tempOrderMenus, savedOrder)

        return OrderCreationResponse(
            savedOrder.id,
            savedOrder.orderSummary!!,
            savedOrder.paymentPrice,
        )
    }

    private fun saveOrderSubEntities(tempOrderMenus: List<OrderMenu>, savedOrder: Order) {
        // 주문 메뉴 생성, 저장
        tempOrderMenus.forEach { tempOrderMenu ->
            val savedOrderMenu = orderMenuRepository.save(
                tempOrderMenu.copy(
                    orderId = savedOrder.id,
                    totalPrice =
                        tempOrderMenu.menuQuantity * (tempOrderMenu.menuPrice + tempOrderMenu.calculateOptionTotalPricePerMenuUnit()),
                ),
            )

            // 주문 메뉴 옵션 그룹 생성, 저장
            tempOrderMenu.orderMenuOptionGroups!!.forEach { tempOrderMenuOptionGroup ->
                val savedOrderMenuOptionGroup = orderMenuOptionGroupRepository.save(
                    tempOrderMenuOptionGroup.copy(orderMenuId = savedOrderMenu.id!!),
                )

                // 주문 메뉴 옵션 생성, 저장
                tempOrderMenuOptionGroup.orderMenuOptions!!.forEach { tempOrderMenuOption ->
                    orderMenuOptionRepository.save(
                        tempOrderMenuOption.copy(orderMenuOptionGroupId = savedOrderMenuOptionGroup.id!!),
                    )
                }
            }
        }
    }

    private fun createTempOrderMenus(orderCreationRequest: OrderCreationRequest, targetMenuEntities: Map<String, Menu>): List<OrderMenu> {
        val tempOrderMenus = orderCreationRequest.orderMenus.map { reqOrderMenu ->
            // 주문 메뉴에 해당하는 메뉴 정보
            val menuEntity = targetMenuEntities[reqOrderMenu.id] ?: throw OrderException.MenuNotFound(reqOrderMenu.id)

            // INSERT 위한 임시 객체 생성. INSERT 시점 copy 하여 값 재설정
            OrderMenu(
                orderId = "",
                menuId = menuEntity.id!!,
                menuName = menuEntity.name!!,
                menuQuantity = reqOrderMenu.quantity,
                menuPrice = menuEntity.getLongTypePrice(), // 주문시점 메뉴의 1개당 기본가격
                totalPrice = 0L, // INSERT 시점에 설정
                orderMenuOptionGroups = reqOrderMenu.orderMenuOptionGroups.map { reqOrderMenuOptionGroup ->
                    // 주문요청 메뉴의 옵션그룹 정보.
                    // 동일메뉴-동일옵션그룹-다른옵션 이라면 동일 그룹이 여러개가 된다.
                    val groupEntities = menuEntity.menuOptionGroup?.filter { it.id == reqOrderMenuOptionGroup.id }
                        ?: throw OrderException.OptionGroupNotFound(reqOrderMenuOptionGroup.id)

                    OrderMenuOptionGroup(
                        orderMenuId = 0L,
                        orderMenuOptionGroupName = groupEntities[0].name!!, //
                        orderMenuOptions = reqOrderMenuOptionGroup.orderMenuOptionIds.map { orderMenuOptionId ->
                            // 주문요청 메뉴에 해당하는 옵션그룹의 옵션 정보.
                            // 동일메뉴-동일옵션그룹-다른옵션 이라면 그룹 내 옵션들을 모두 봐야 함.
                            val optionEntity = groupEntities
                                .flatMap { it.menuOption ?: emptyList() }
                                .find { it.id == orderMenuOptionId }
                                ?: throw OrderException.OptionNotFound(orderMenuOptionId)

                            OrderMenuOption(
                                orderMenuOptionGroupId = 0L,
                                menuOptionName = optionEntity.name!!,
                                menuOptionPrice = optionEntity.getLongTypePrice(),
                            )
                        },
                    )
                },
            )
        }
        return tempOrderMenus
    }

    private fun Menu.getLongTypePrice(): Long {
        // 현재 테스트데이터 형식 4,000 / 5000 처럼 문자열임
        val tmpPrice = this.price?.replace(",", "")
        return tmpPrice
            .runCatching { tmpPrice?.toLong() }
            .onFailure { throw OrderException("가격 정보를 파싱할 수 없습니다.") }
            .getOrNull() ?: throw OrderException("가격 정보가 없습니다.")
    }

    private fun MenuOption.getLongTypePrice(): Long {
        // 옵션 가격은 Long?
        return this.price ?: throw OrderException("가격 정보가 없습니다.")
    }
}
