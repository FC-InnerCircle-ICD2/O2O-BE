package org.fastcampus.applicationclient.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@EnableAsync
@Configuration
class AsyncConfig : AsyncConfigurer {
    @Bean
    override fun getAsyncExecutor(): Executor? {
        return ThreadPoolTaskExecutor().apply {
            val cpuCoreCount = Runtime.getRuntime().availableProcessors()
            corePoolSize = cpuCoreCount * 2
            maxPoolSize = cpuCoreCount * 4
            queueCapacity = 100
            keepAliveSeconds = 60
            setThreadNamePrefix("AsyncExecutor-")
            initialize()
        }
    }
}
