package org.fastcampus.applicationoss.scheduler.store

import org.fastcampus.applicationoss.scheduler.JobRunner
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.Trigger
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component

/**
 * Created by brinst07 on 25. 2. 27.
 */
@Component
class StoreScheduler(
    private val scheduler: Scheduler,
): JobRunner() {
    override fun doRun(args: ApplicationArguments) {
        val jobDetail: JobDetail = this.buildJobDetail(StoreSchedulerJob::class.java, "storeJob", "storeBatch", emptyMap())
        val trigger: Trigger = buildJobTrigger("0/10 * * * * ?")
        scheduler.scheduleJob(jobDetail, trigger)
    }
}
