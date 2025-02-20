package org.fastcampus.applicationoss.scheduler.payment

import org.fastcampus.applicationoss.scheduler.JobRunner
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.Trigger
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component

@Component
class RefundScheduler(
    private val scheduler: Scheduler,
) : JobRunner() {
    override fun doRun(args: ApplicationArguments) {
        val jobDetail: JobDetail = this.buildJobDetail(RefundScheduleJob::class.java, "refundJob", "batch", emptyMap())
        val trigger: Trigger = buildJobTrigger("0 0/1 * * * ?")
        scheduler.scheduleJob(jobDetail, trigger)
    }
}
