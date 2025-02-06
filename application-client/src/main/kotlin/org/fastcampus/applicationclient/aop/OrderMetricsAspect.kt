package org.fastcampus.applicationclient.aop

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.fastcampus.order.entity.Order
import org.springframework.stereotype.Component

@Aspect
@Component
class OrderMetricsAspect(
    private val registry: MeterRegistry,
) {
    @Around("@annotation(OrderMetered)")
    fun measureOrderOperation(joinPoint: ProceedingJoinPoint): Any? {
        val sample = Timer.start()
        val version = extractVersion(joinPoint)
        val operation = extractOperation(joinPoint)

        return try {
            val result = joinPoint.proceed()

            // 주문 API 요청 메트릭
            Counter.builder("order_api_requests_total")
                .tag("version", version)
                .tag("operation", operation)
                .register(registry)
                .increment()

            // 주문 상태 메트릭
            if (operation == "createOrder" || operation == "cancelOrder") {
                val order = when {
                    result is Order -> result
                    joinPoint.args.isNotEmpty() && joinPoint.args[0] is Order -> joinPoint.args[0] as Order
                    else -> null
                }

                order?.let {
                    Counter.builder("order_status_total")
                        .tag("status", it.status.name)
                        .register(registry)
                        .increment()
                }
            }

            // 결제 처리 메트릭
            if (operation == "approveOrderPayment") {
                Counter.builder("payment_processing_total")
                    .tag("type", "payment")
                    .tag("status", "success")
                    .register(registry)
                    .increment()
            }

            // 처리 시간 메트릭
            sample.stop(
                Timer.builder("order_processing_duration_seconds")
                    .tag("version", version)
                    .tag("operation", operation)
                    .register(registry),
            )

            result
        } catch (e: Exception) {
            // 주문 실패 메트릭
            Counter.builder("order_failures_total")
                .tag("version", version)
                .tag("operation", operation)
                .tag("error", e.javaClass.simpleName)
                .register(registry)
                .increment()

            // 결제 실패 메트릭
            if (operation == "refundOrder") {
                Counter.builder("payment_processing_total")
                    .tag("type", "refund")
                    .tag("status", "failure")
                    .register(registry)
                    .increment()
            }

            throw e
        }
    }
}

private fun extractVersion(joinPoint: ProceedingJoinPoint): String {
    val annotation = (joinPoint.signature as MethodSignature)
        .method
        .getAnnotation(OrderMetered::class.java)
    return annotation.version
}

private fun extractOperation(joinPoint: ProceedingJoinPoint): String {
    return joinPoint.signature.name
}
