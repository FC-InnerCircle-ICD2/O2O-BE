package org.fastcampus.applicationoss.scheduler.payment

import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
class RefundScheduleJob(
    private val refundJob: Job,
    private val jobLauncher: JobLauncher,
) : QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        try {
            jobLauncher.run(refundJob, JobParameters())
        } catch (e: Exception) {
            throw JobExecutionException(e)
        }
    }
}
