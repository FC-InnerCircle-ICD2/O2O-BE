package org.fastcampus.applicationoss.scheduler

import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.Job
import org.quartz.JobBuilder.newJob
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.Trigger
import org.quartz.TriggerBuilder.newTrigger
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner

abstract class JobRunner : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        doRun(args)
    }

    protected abstract fun doRun(args: ApplicationArguments)

    fun buildJobTrigger(scheduleExp: String): Trigger {
        return newTrigger()
            .withSchedule(cronSchedule(scheduleExp))
            .build()
    }

    fun <T : Job> buildJobDetail(jobClass: Class<T>, name: String, group: String, params: Map<String, Any>): JobDetail {
        val jobDataMap = JobDataMap(params)

        return newJob(jobClass)
            .withIdentity(name, group)
            .usingJobData(jobDataMap)
            .build()
    }
}
