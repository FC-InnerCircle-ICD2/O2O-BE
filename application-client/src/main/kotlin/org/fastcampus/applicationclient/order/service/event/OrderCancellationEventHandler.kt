package org.fastcampus.applicationclient.order.service.event

import com.fasterxml.jackson.databind.ObjectMapper
import org.fastcampus.order.event.NotificationSender
import org.fastcampus.store.repository.StoreRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderCancellationEventHandler(
    private val storeRepository: StoreRepository,
    private val objectMapper: ObjectMapper,
    @Qualifier("OrderCancellationSender")
    private val orderCancellationSender: NotificationSender,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(OrderCancellationEventHandler::class.java)
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleEvent(event: OrderCancellationEvent) {
        logger.debug("주문취소 이벤트 처리: {}", event)

        val ownerId = storeRepository.findOwnerIdByStoreId(event.storeId)
        logger.debug("가게ID [{}]으로 점주ID 찾기 결과: [{}]", event.storeId, ownerId)

        val stringMessage = objectMapper.writeValueAsString(
            mapOf(
                "ownerId" to ownerId,
                "orderId" to event.orderId,
            ),
        )

        orderCancellationSender.send(stringMessage)
    }
}
