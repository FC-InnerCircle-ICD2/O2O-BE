package org.fastcampus.order.repository

import org.fastcampus.order.exception.OrderException
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Component
class OrderLockManagerCustom(
    private val redisTemplate: RedisTemplate<String, Any>,
) : OrderLockManager {
    // 외부에서 전달받을까도 고민했으나 Order Lock 에 대한 책임만 가지고 있으므로 내부에서 가져도 된다고 판단.
    // 외부에서 받을 경우 실제 서비스로직을 봐야하기에 더욱더 북잡해진다고 판단.
    private val maxAttempts = 3
    private val backoffMillis = 100L // 0.1초
    private val lockExpireSecond = 30L // 30초

    override fun <R> lock(orderId: String, function: () -> R): R {
        val lockKey = "order_lock:$orderId"
        var attempts = 0
        while (attempts < maxAttempts) {
            val success = tryAcquireLock(lockKey)
            if (success) {
                logger.debug("Lock acquired for orderId: {}", orderId)
                try {
                    return function()
                } finally {
                    releaseLock(lockKey)
                    logger.debug("Lock released for orderId: {}", orderId)
                }
            } else {
                attempts++
                Thread.sleep(backoffMillis)
            }
        }
        throw OrderException.OrderLockException(orderId)
    }

    // redis 의 이슈로 lock 획득이 안되어도 프로세스는 진행
    private fun tryAcquireLock(lockKey: String): Boolean {
        return redisTemplate.opsForValue()
            .setIfAbsent(lockKey, "locked", lockExpireSecond.seconds.toJavaDuration()) ?: true
    }

    private fun releaseLock(lockKey: String) {
        redisTemplate.delete(lockKey)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OrderLockManager::class.java)
    }
}
