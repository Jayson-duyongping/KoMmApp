package com.jayson.komm.dev.service

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import com.jayson.komm.common.util.LogUtils

/**
 * JobScheduler
 */
class DevJobService : JobService() {

    companion object {
        private const val TAG = "DevJobService"
    }

    override fun onStartJob(params: JobParameters): Boolean {
        // 打印日志
        LogUtils.d(TAG, "onStartJob")
        // 重新调度任务
        scheduleJob()
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return false
    }

    private fun scheduleJob() {
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        val jobInfo = JobInfo.Builder(1, ComponentName(this, DevJobService::class.java))
            .setMinimumLatency(10000) // 延迟10秒执行
            .setPersisted(true) // 设备重启后继续执行
            .build()

        jobScheduler.schedule(jobInfo)
    }
}