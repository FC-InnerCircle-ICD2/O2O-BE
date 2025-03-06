package org.fastcampus.applicationclient.review.service

import org.fastcampus.applicationclient.fixture.createAuthMember
import org.fastcampus.applicationclient.fixture.createOrderFixture
import org.fastcampus.applicationclient.fixture.createReview
import org.fastcampus.applicationclient.fixture.createStore
import org.fastcampus.applicationclient.review.controller.dto.WritableReviewResponse
import org.fastcampus.order.entity.Order
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.review.entity.Review
import org.fastcampus.review.repository.ReviewRepository
import org.fastcampus.store.repository.StoreRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@ExtendWith(MockitoExtension::class)
class ReviewServiceTest {
    @Mock private lateinit var reviewImageManager: ReviewImageManager

    @Mock private lateinit var reviewRepository: ReviewRepository

    @Mock private lateinit var orderRepository: OrderRepository

    @Mock private lateinit var storeRepository: StoreRepository

    @InjectMocks private lateinit var reviewService: ReviewService

    @Test
    fun `should find writable reviews`() {
        val authMember = createAuthMember()
        val store = createStore(id = "store_001")
        val reviewableOrder = createOrderFixture(id = "order_3", storeId = store.id, status = Order.Status.COMPLETED)
        val orders: List<Order> = listOf(
            createOrderFixture(id = "order_1", storeId = store.id),
            createOrderFixture(id = "order_2", storeId = store.id),
            reviewableOrder,
        )

        val reviews: List<Review> = listOf(
            createReview(id = 1L, orderId = "order_1"),
            createReview(id = 1L, orderId = "order_2"),
        )

        `when`(orderRepository.findReviewableOrders(authMember.id)).thenReturn(orders)
        `when`(reviewRepository.findByOrderIdIn(orders.map { it.id }.toList())).thenReturn(reviews)
        `when`(storeRepository.findById(store.id!!)).thenReturn(store)

        val writableReviewResponses: List<WritableReviewResponse> = reviewService.findWritableReview(authMember)

        expectThat {
            expectThat(writableReviewResponses.size).isEqualTo(1)
            expectThat(writableReviewResponses[0].storeId).isEqualTo(store.id)
            expectThat(writableReviewResponses[0].storeName).isEqualTo(store.name)
            expectThat(writableReviewResponses[0].orderId).isEqualTo(reviewableOrder.id)
            expectThat(writableReviewResponses[0].orderTime).isEqualTo(reviewableOrder.orderTime)
            expectThat(writableReviewResponses[0].orderSummary).isEqualTo(reviewableOrder.orderSummary)
        }

        verify(orderRepository).findReviewableOrders(authMember.id)
        verify(reviewRepository).findByOrderIdIn(orders.map { it.id }.toList())
        verify(storeRepository).findById(store.id!!)
    }
}
