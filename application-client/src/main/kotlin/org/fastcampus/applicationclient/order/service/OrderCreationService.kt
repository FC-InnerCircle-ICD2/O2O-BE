package org.fastcampus.applicationclient.order.service

import org.fastcampus.applicationclient.aop.OrderMetered
import org.fastcampus.applicationclient.order.controller.dto.request.OrderCreationRequest
import org.fastcampus.applicationclient.order.controller.dto.response.OrderCreationResponse
import org.fastcampus.member.entity.Member
import org.fastcampus.member.repository.MemberRepository
import org.fastcampus.order.entity.Order
import org.fastcampus.order.entity.OrderMenu
import org.fastcampus.order.entity.OrderMenuOption
import org.fastcampus.order.entity.OrderMenuOptionGroup
import org.fastcampus.order.exception.OrderException
import org.fastcampus.order.repository.OrderDetailRepository
import org.fastcampus.order.repository.OrderMenuOptionGroupRepository
import org.fastcampus.order.repository.OrderMenuOptionRepository
import org.fastcampus.order.repository.OrderMenuRepository
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.payment.entity.Payment
import org.fastcampus.payment.repository.PaymentRepository
import org.fastcampus.store.entity.Menu
import org.fastcampus.store.entity.MenuOption
import org.fastcampus.store.entity.Store
import org.fastcampus.store.repository.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class OrderCreationService(
    private val memberRepository: MemberRepository,
    private val orderRepository: OrderRepository,
    private val orderDetailRepository: OrderDetailRepository,
    private val storeRepository: StoreRepository,
    private val paymentRepository: PaymentRepository,
    private val orderMenuRepository: OrderMenuRepository,
    private val orderMenuOptionGroupRepository: OrderMenuOptionGroupRepository,
    private val orderMenuOptionRepository: OrderMenuOptionRepository,
) {
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

        // 전체 주문금액
        val totalPrice = tempOrderMenus.sumOf { it.calculateMenuPrice() }
        if (totalPrice < storeEntity.minimumOrderAmount) {
            throw OrderException.MinimumOrderAmountNotSatisfied()
        }

        // 결제 생성, 저장
        val savedPayment = savePayment(orderCreationRequest.paymentType, totalPrice)

        // 주문 생성, 저장
        val summary = getSummary(tempOrderMenus)
        val savedOrder = orderRepository.save(
            of(orderCreationRequest, storeEntity, loginMember, savedPayment, summary),
        )

        // 주문 메뉴, 옵션그룹, 옵션 저장
        val subEntities = saveOrderSubEntities(tempOrderMenus, savedOrder)

        // 저장된 주문정보 전체
        val orderEntity = savedOrder.copy(orderMenus = subEntities)

        orderDetailRepository.saveOrder(orderEntity, mapOf("code" to savedPayment.type.code, "desc" to savedPayment.type.desc))

        return OrderCreationResponse(
            savedOrder.id,
            savedOrder.orderSummary!!,
            savedOrder.paymentPrice,
        )
    }

    private fun of(
        orderCreationRequest: OrderCreationRequest,
        storeEntity: Store,
        loginMember: Member,
        savedPayment: Payment,
        summary: String,
    ): Order {
        val currentTime = LocalDateTime.now()
        val orderId = getOrderId(currentTime)

        return Order(
            id = orderId,
            storeId = storeEntity.id,
            storeName = storeEntity.name,
            storeImageThumbnail = storeEntity.imageThumbnail,
            userId = loginMember.id,
            roadAddress = orderCreationRequest.roadAddress,
            jibunAddress = orderCreationRequest.jibunAddress,
            detailAddress = orderCreationRequest.detailAddress,
            excludingSpoonAndFork = orderCreationRequest.excludingSpoonAndFork ?: true,
            requestToRider = orderCreationRequest.requestToRider,
            tel = loginMember.phone,
            status = Order.Status.WAIT,
            orderTime = currentTime,
            orderSummary = summary,
            type = orderCreationRequest.orderType,
            paymentId = savedPayment.id!!,
            isDeleted = false,
            deliveryCompleteTime = null,
            deliveryPrice = 0L,
            orderPrice = savedPayment.paymentPrice!!,
            paymentPrice = savedPayment.paymentPrice!!,
        )
    }

    private fun getSummary(tempOrderMenus: List<OrderMenu>): String {
        val menuSize = tempOrderMenus.size
        val summary = if (menuSize == 1) tempOrderMenus[0].menuName else "${tempOrderMenus[0].menuName} 외 ${menuSize - 1}개"
        return summary
    }

    private fun getOrderId(currentTime: LocalDateTime): String {
        val today = currentTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val uuid8charsInFront = UUID.randomUUID().toString().uppercase().substring(0..7)
        return "$today-$uuid8charsInFront"
    }

    private fun savePayment(paymentType: Payment.Type, totalPrice: Long): Payment {
        val savedPayment = paymentRepository.save(
            Payment(
                type = paymentType,
                paymentPrice = totalPrice,
            ),
        )
        return savedPayment
    }

    private fun saveOrderSubEntities(tempOrderMenus: List<OrderMenu>, savedOrder: Order): List<OrderMenu> {
        // 주문 메뉴 생성, 저장후 결과 반환
        return tempOrderMenus.map { tempOrderMenu ->
            val savedOrderMenu = orderMenuRepository.save(
                tempOrderMenu.copy(
                    orderId = savedOrder.id,
                    totalPrice = tempOrderMenu.calculateMenuPrice(),
                ),
            )

            // 주문 메뉴 옵션 그룹 생성, 저장
            val savedOrderMenuOptionGroups = tempOrderMenu.orderMenuOptionGroups!!.map { tempOrderMenuOptionGroup ->
                val savedOrderMenuOptionGroup = orderMenuOptionGroupRepository.save(
                    tempOrderMenuOptionGroup.copy(orderMenuId = savedOrderMenu.id!!),
                )

                // 주문 메뉴 옵션 생성, 저장
                val savedOrderMenuOptions = tempOrderMenuOptionGroup.orderMenuOptions!!.map { tempOrderMenuOption ->
                    orderMenuOptionRepository.save(
                        tempOrderMenuOption.copy(orderMenuOptionGroupId = savedOrderMenuOptionGroup.id!!),
                    )
                }

                // 주문 옵션 그룹에 옵션을 조립
                savedOrderMenuOptionGroup.copy(orderMenuOptions = savedOrderMenuOptions)
            }

            // 주문 메뉴에 주문 옵션 그룹 조립
            savedOrderMenu.copy(orderMenuOptionGroups = savedOrderMenuOptionGroups)
        }
    }

    private fun createTempOrderMenus(orderCreationRequest: OrderCreationRequest, targetMenuEntities: Map<String, Menu>): List<OrderMenu> {
        // 주문요청 메뉴 목록
        val tempOrderMenus = orderCreationRequest.orderMenus.map { reqOrderMenu ->
            // 주문에 해당하는 메뉴 정보
            val menuEntity = targetMenuEntities[reqOrderMenu.id] ?: throw OrderException.MenuNotFound(reqOrderMenu.id)

            // INSERT 위한 임시 객체 생성.
            OrderMenu(
                orderId = "", // INSERT 시점에 설정
                menuId = menuEntity.id!!,
                menuName = menuEntity.name ?: "Unknown",
                menuQuantity = reqOrderMenu.quantity,
                menuPrice = menuEntity.getLongTypePrice(), // 메뉴의 1개당 기본가격
                totalPrice = 0L, // 조립 후 INSERT 시점에 설정
                orderMenuOptionGroups = reqOrderMenu.orderMenuOptionGroups.map { reqOrderMenuOptionGroup ->
                    // 주문요청 메뉴의 옵션그룹 정보.
                    val groupEntities = menuEntity.menuOptionGroup?.filter { it.id == reqOrderMenuOptionGroup.id }
                        ?: throw OrderException.OptionGroupNotFound(reqOrderMenuOptionGroup.id)

                    OrderMenuOptionGroup(
                        orderMenuId = 0L,
                        orderMenuOptionGroupName = groupEntities[0].name ?: "Unknown",
                        orderMenuOptions = reqOrderMenuOptionGroup.orderMenuOptionIds.map { orderMenuOptionId ->
                            // 주문요청 메뉴에 해당하는 옵션그룹의 옵션 정보.
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
