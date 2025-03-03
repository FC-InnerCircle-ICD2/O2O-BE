package org.fastcampus.applicationoss.batch.job.payment

import org.fastcampus.applicationoss.batch.tasklet.payment.PaymentGatewayFactory
import org.fastcampus.applicationoss.batch.tasklet.payment.RefundTasklet
import org.fastcampus.payment.repository.PaymentRepository
import org.fastcampus.payment.repository.RefundRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class RefundJobConfiguration(
    private val refundRepository: RefundRepository,
    private val paymentGatewayFactory: PaymentGatewayFactory,
    private val paymentRepository: PaymentRepository,
) {
    @Bean
    @Throws(Exception::class)
    fun refundJob(jobRepository: JobRepository, refundStep: Step): Job {
        return JobBuilder("refundJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(refundStep)
            .build()
    }

    @Bean
    fun refundStep(jobRepository: JobRepository, transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("refundStep", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(RefundTasklet(refundRepository, paymentGatewayFactory, paymentRepository), transactionManager)
            .build()
    }
}
