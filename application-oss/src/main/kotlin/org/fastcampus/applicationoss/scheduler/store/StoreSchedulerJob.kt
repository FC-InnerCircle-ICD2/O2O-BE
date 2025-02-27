package org.fastcampus.applicationoss.scheduler.store

import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

/**
 * Created by brinst07 on 25. 2. 27.
 */
@Component
class StoreSchedulerJob(
    private val storeJob: Job,
    private val jobLauncher: JobLauncher,
): QuartzJobBean()  {
    override fun executeInternal(context: JobExecutionContext) {
        try {
            jobLauncher.run(storeJob, JobParameters())
        } catch (e: Exception) {
            throw JobExecutionException(e)
        }
    }
}
