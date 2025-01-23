package org.fastcampus.applicationadmin.event

import org.fastcampus.order.event.NotificationReceiver
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class RedisMessageConfig(
    @Qualifier("OrderNotificationReceiver")
    private val notificationReceiver: NotificationReceiver,
) {
    @Bean
    fun redisContainer(
        connectionFactory: RedisConnectionFactory,
        @Qualifier("OrderNotificationReceiverAdapter") listenerAdapter: MessageListenerAdapter,
    ): RedisMessageListenerContainer {
        return RedisMessageListenerContainer()
            .apply {
                setConnectionFactory(connectionFactory)
                addMessageListener(listenerAdapter, PatternTopic("ORDER_NOTIFICATION"))
                setTaskExecutor(
                    ThreadPoolTaskExecutor().apply {
                        val cpuCoreCount = Runtime.getRuntime().availableProcessors()
                        corePoolSize = cpuCoreCount * 2
                        maxPoolSize = cpuCoreCount * 4
                        queueCapacity = 100
                        keepAliveSeconds = 60
                        setThreadNamePrefix("MsgListener-")
                        initialize()
                    },
                )
            }
    }

    @Bean("OrderNotificationReceiverAdapter")
    fun listenerAdapter(): MessageListenerAdapter {
        return MessageListenerAdapter(notificationReceiver)
    }

    @Bean
    fun stringRedisTemplate(connectionFactory: RedisConnectionFactory): StringRedisTemplate {
        return StringRedisTemplate(connectionFactory)
    }
}
