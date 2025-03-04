package org.fastcampus.applicationoss.batch.job.store

import org.fastcampus.applicationoss.batch.tasklet.store.StoreTasklet
import org.fastcampus.order.repository.OrderRepository
import org.fastcampus.store.repository.StoreRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

/**
 * Created by brinst07 on 25. 2. 27.
 */
@Configuration
class StoreJobConfiguration(
    private val storeRepository: StoreRepository,
    private val orderRepository: OrderRepository,
) {
    @Bean
    @Throws(Exception::class)
    fun storeJob(jobRepository: JobRepository, storeStep: Step): Job {
        return JobBuilder("storeJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(storeStep)
            .build()
    }


    @Bean
    fun storeStep(jobRepository: JobRepository, transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("storeStep", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(StoreTasklet(storeRepository, orderRepository), transactionManager)
            .build()
    }
}
