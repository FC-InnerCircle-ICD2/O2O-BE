package org.fastcampus.order.exception

open class OrderException(message: String) : RuntimeException(message) {
    class MinimumOrderAmountNotSatisfied() : OrderException("최소 주문금액이 충족되지 않습니다.")

    data class OrderNotFound(val orderId: String) : OrderException("주문을 찾을 수 없습니다.")

    data class OrderCanNotAccept(val orderId: String) : OrderException("주문 수락이 불가능한 주문입니다.")

    data class OrderCanNotRefuse(val orderId: String) : OrderException("주문 거절이 불가능한 주문입니다.")

    data class OrderCanNotComplete(val orderId: String) : OrderException("주문 완료는 수락된 주문에 한해서 가능합니다.")

    data class OrderCanNotCancelled(val orderId: String) : OrderException("해당 주문은 취소할 수 없습니다.")

    data class StoreNotFound(val storeId: String) : OrderException("가게를 찾을 수 없습니다.")

    data class StoreClosed(val storeId: String) : OrderException("가게의 영업이 종료되어 주문이 불가능합니다.")

    data class MissingRequiredOptionGroup(val optionGroupId: String) : OrderException("필수 옵션그룹이 누락되었습니다.")

    data class OptionGroupNotFound(val optionGroupId: String) : OrderException("옵션그룹 정보를 찾을 수 없습니다.")

    data class OptionNotFound(val optionId: String) : OrderException("옵션 정보를 찾을 수 없습니다.")

    data class WeiredOptionGroupInfo(val optionGroupId: String) : OrderException("등록된 옵션그룹 정보에 이상이 있습니다.")

    data class OutOfOptionSelectionRange(val optionId: String) : OrderException("그룹 내 옵션 선택범위가 올바르지 않습니다.")

    class MenuCategoryNotFound : OrderException("가게에 등록된 메뉴 카테고리 정보가 없습니다.")

    class MissingOrderMenu : OrderException("주문 요청에 메뉴정보가 없습니다.")

    data class SoldOutMenu(val menuId: String) : OrderException("품절 메뉴가 존재합니다.")

    data class MenuNotFound(val menuId: String) : OrderException("메뉴 정보를 찾을 수 없습니다.")

    class AddressRequired() : OrderException("도로명 주소와 지번 주소중 하나는 필수 입니다.")
}
