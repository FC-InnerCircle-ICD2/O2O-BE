package org.fastcampus.applicationclient.config

import io.micrometer.core.instrument.MeterRegistry
import org.fastcampus.applicationclient.aop.MemberMetricsAspect
import org.fastcampus.applicationclient.aop.OrderMetricsAspect
import org.fastcampus.applicationclient.aop.ReviewMetricsAspect
import org.fastcampus.applicationclient.aop.StoreMetricsAspect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy

@Configuration
@EnableAspectJAutoProxy
class MetricsConfig {
    @Bean
    fun storeMetricsAspect(registry: MeterRegistry): StoreMetricsAspect {
        return StoreMetricsAspect(registry)
    }

    @Bean
    fun orderMetricsAspect(registry: MeterRegistry): OrderMetricsAspect {
        return OrderMetricsAspect(registry)
    }

    @Bean
    fun memberMetricsAspect(registry: MeterRegistry): MemberMetricsAspect {
        return MemberMetricsAspect(registry)
    }

    @Bean
    fun reviewMetricsAspect(registry: MeterRegistry): ReviewMetricsAspect {
        return ReviewMetricsAspect(registry)
    }
}
