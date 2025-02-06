package org.fastcampus.applicationclient.aop

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component

@Aspect
@Component
class StoreMetricsAspect(
    private val registry: MeterRegistry,
) {
    @Around("@annotation(StoreMetered)")
    fun measureStoreOperation(joinPoint: ProceedingJoinPoint): Any? {
        val sample = Timer.start()
        val version = extractVersion(joinPoint)
        val operation = extractOperation(joinPoint)

        return try {
            val result = joinPoint.proceed()

            // 스토어 api 성공 메트릭
            Counter.builder("store_api_requests_total")
                .tag("version", version)
                .tag("operation", operation)
                .register(registry)
                .increment()

            // 검색어 트렌드 메트릭 추가
            if (operation == "search") {
                val keyword = joinPoint.args.firstOrNull() as? String
                if (keyword != null) {
                    Counter.builder("store_search_keywords_total")
                        .tag("keyword", keyword)
                        .register(registry)
                        .increment()
                }
            }

            sample.stop(
                Timer.builder("store_operation_duration_seconds")
                    .tag("version", version)
                    .tag("operation", operation)
                    .register(registry),
            )
            result
        } catch (e: Exception) {
            // 스토어 api 실패 매트릭
            Counter.builder("store_operation_failures_total")
                .tag("version", version)
                .tag("operation", operation)
                .tag("error", e.javaClass.simpleName)
                .register(registry)
                .increment()

            throw e
        }
    }

    private fun extractVersion(joinPoint: ProceedingJoinPoint): String {
        val annotation = (joinPoint.signature as MethodSignature)
            .method
            .getAnnotation(StoreMetered::class.java)
        return annotation.version
    }

    private fun extractOperation(joinPoint: ProceedingJoinPoint): String {
        return joinPoint.signature.name
    }
}
