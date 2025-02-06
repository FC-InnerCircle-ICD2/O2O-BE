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
class MemberMetricsAspect(
    private val registry: MeterRegistry,
) {
    @Around("@annotation(MemberMetered)")
    fun measureMemberOperation(joinPoint: ProceedingJoinPoint): Any? {
        val sample = Timer.start()
        val version = extractVersion(joinPoint)
        val operation = extractOperation(joinPoint)

        return try {
            val result = joinPoint.proceed()

            // API 요청 메트릭
            Counter.builder("member_api_requests_total")
                .tag("version", version)
                .tag("operation", operation)
                .register(registry)
                .increment()

            // 회원 상태 메트릭
            if (operation == "join") {
                Counter.builder("member_status_total")
                    .tag("status", "JOIN")
                    .register(registry)
                    .increment()
            }

            // 인증 처리 메트릭
            if (operation in listOf("refresh", "logout")) {
                Counter.builder("auth_processing_total")
                    .tag("type", operation)
                    .tag("status", "success")
                    .register(registry)
                    .increment()
            }

            sample.stop(
                Timer.builder("member_processing_duration_seconds")
                    .tag("version", version)
                    .tag("operation", operation)
                    .register(registry),
            )

            result
        } catch (e: Exception) {
            Counter.builder("member_failures_total")
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
            .getAnnotation(MemberMetered::class.java)
        return annotation.version
    }

    private fun extractOperation(joinPoint: ProceedingJoinPoint): String {
        return joinPoint.signature.name
    }
}
