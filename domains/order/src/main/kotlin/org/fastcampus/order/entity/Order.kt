package org.fastcampus.order.entity

import org.fastcampus.order.exception.OrderException
import java.time.LocalDateTime

/**
 * Created by brinst07 on 25. 1. 11..
 */

data class Order(
    val id: String,
    val storeId: String?,
    val userId: Long?,
    val roadAddress: String?,
    val jibunAddress: String?,
    val detailAddress: String?,
    val tel: String?,
    var status: Status,
    val orderTime: LocalDateTime,
    val orderSummary: String?,
    val type: Type,
    val paymentId: Long,
    val isDeleted: Boolean,
    val deliveryCompleteTime: LocalDateTime?,
    val orderPrice: Long,
    val deliveryPrice: Long?,
    val paymentPrice: Long,
    val excludingSpoonAndFork: Boolean = true,
    val requestToRider: String? = null,
    val orderMenus: List<OrderMenu>? = null, // 필요시 설정
) {
    fun accept() {
        // 주문접수상태만 주문수락가능
        if (this.status != Status.RECEIVE) {
            throw OrderException.OrderCanNotAccept(this.id)
        }
        this.status = Status.ACCEPT
    }

    fun cancel() {
        // 주문접수상태만 주문취소가능
        if (this.status != Status.RECEIVE) {
            throw OrderException.OrderCanNotCancelled(this.id)
        }
        this.status = Status.CANCEL
    }

    fun refuse() {
        // 주문접수상태만 주문거절가능
        if (this.status != Status.RECEIVE) {
            throw OrderException.OrderCanNotRefuse(this.id)
        }
        this.status = Status.REFUSE
    }

    fun complete() {
        // 주문수락상태에서 주문완료가능
        if (this.status != Status.ACCEPT) {
            throw OrderException.OrderCanNotComplete(this.id)
        }
        this.status = Status.COMPLETED
    }

    enum class Status(
        val code: String,
        val desc: String,
    ) {
        WAIT("S1", "주문대기"),
        RECEIVE("S2", "주문접수"),
        ACCEPT("S3", "주문수락"),
        REFUSE("S4", "주문거절"),
        COMPLETED("S5", "주문완료"),
        CANCEL("S6", "주문취소"),
        ;

        fun toClientStatus(): ClientStatus {
            return when (this) {
                RECEIVE -> ClientStatus.NEW
                ACCEPT -> ClientStatus.ONGOING
                COMPLETED -> ClientStatus.DONE
                CANCEL -> ClientStatus.CANCEL
                REFUSE -> ClientStatus.REFUSE
                else -> throw IllegalArgumentException("Unknown status $this")
            }
        }
    }

    enum class ClientStatus(
        val code: String,
        val desc: String,
    ) {
        NEW("C1", "신규"),
        ONGOING("C2", "진행중"),
        DONE("C3", "완료"),
        CANCEL("C4", "취소"),
        REFUSE("C5", "거절"),
        ;

        fun toOrderStatus(): Status {
            return when (this) {
                NEW -> Status.RECEIVE
                ONGOING -> Status.ACCEPT
                DONE -> Status.COMPLETED
                CANCEL -> Status.CANCEL
                REFUSE -> Status.REFUSE
            }
        }
    }

    enum class Type(
        val code: String,
        val desc: String,
    ) {
        DELIVERY("T1", "배달"),
        PACKING("T2", "포장"),
    }
}
