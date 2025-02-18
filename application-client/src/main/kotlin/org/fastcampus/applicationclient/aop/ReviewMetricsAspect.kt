package org.fastcampus.applicationclient.aop

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.fastcampus.common.dto.TimeBasedCursorDTO
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Aspect
@Component
class ReviewMetricsAspect(
    private val registry: MeterRegistry,
) {
    @Around("@annotation(ReviewMetered)")
    fun measureReviewOperation(joinPoint: ProceedingJoinPoint): Any? {
        val sample = Timer.start()
        val version = extractVersion(joinPoint)
        val operation = extractOperation(joinPoint)

        return try {
            val result = joinPoint.proceed()

            // API 요청 메트릭
            Counter.builder("review_api_requests_total")
                .tag("version", version)
                .tag("operation", operation)
                .register(registry)
                .increment()

            // 이미지 업로드 메트릭
            if (operation == "upload") {
                Counter.builder("review_image_upload_total")
                    .tag("status", "success")
                    .register(registry)
                    .increment()

                // 이미지 크기 메트릭
                val imageFile = joinPoint.args.firstOrNull { it is MultipartFile } as? MultipartFile
                imageFile?.let {
                    Gauge.builder("review_image_size_bytes", { it.size.toDouble() })
                        .tag("operation", operation)
                        .register(registry)
                }
            }

            // 리뷰 작성 가능 조회 메트릭
            if (operation == "findWritableReview") {
                val response = result as? TimeBasedCursorDTO<*>
                Counter.builder("review_writable_count_total")
                    .tag("count", response?.content?.size?.toString() ?: "0")
                    .register(registry)
                    .increment()
            }

            sample.stop(
                Timer.builder("review_processing_duration_seconds")
                    .tag("version", version)
                    .tag("operation", operation)
                    .tag("status", "success") // 또는 "failure"
                    .register(registry),
            )

            result
        } catch (e: Exception) {
            // 실패 메트릭
            Counter.builder("review_failures_total")
                .tag("version", version)
                .tag("operation", operation)
                .tag("error", e.javaClass.simpleName)
                .register(registry)
                .increment()

            // 이미지 업로드 실패 메트릭
            if (operation == "upload") {
                Counter.builder("review_image_upload_total")
                    .tag("status", "failure")
                    .register(registry)
                    .increment()
            }

            throw e
        }
    }

    private fun extractVersion(joinPoint: ProceedingJoinPoint): String {
        val annotation = (joinPoint.signature as MethodSignature)
            .method
            .getAnnotation(ReviewMetered::class.java)
        return annotation.version
    }

    private fun extractOperation(joinPoint: ProceedingJoinPoint): String {
        return joinPoint.signature.name
    }
}
