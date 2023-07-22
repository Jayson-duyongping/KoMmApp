package com.jayson.komm.dev

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.util.LogUtils
import com.jayson.komm.dev.databinding.ActivityDevBinding
import com.jayson.komm.dev.service.DevBroadcastReceiver
import com.jayson.komm.dev.service.DevJobService
import java.util.concurrent.TimeUnit

class DevActivity : BaseActivity() {

    companion object {
        private const val TAG = "DevActivity"
    }

    private lateinit var binding: ActivityDevBinding

    override fun initView() {
        super.initView()
        // 初始化binding
        binding = ActivityDevBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.jobBtn.setOnClickListener {
            // 启动JobScheduler
            startJobScheduler()
        }
        binding.alarmBtn.setOnClickListener {
            // 启动定时器
            startTimer()
        }
        binding.workBtn.setOnClickListener {
            startWork()
        }
    }

    private fun startJobScheduler() {
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfo = JobInfo.Builder(1, ComponentName(this, DevJobService::class.java))
            .setMinimumLatency(10000) // 延迟10秒执行
            .setPersisted(true) // 设备重启后继续执行
            .build()
        jobScheduler.schedule(jobInfo)
    }

    @SuppressLint("ShortAlarm")
    private fun startTimer() {
        // 初始化AlarmManager和PendingIntent
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, DevBroadcastReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        // 设置定时器每隔10秒触发一次
        val intervalMillis: Long = 10000
        val triggerAtMillis = SystemClock.elapsedRealtime() + intervalMillis
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerAtMillis,
            intervalMillis,
            pendingIntent
        )
    }

    private fun startWork() {
        val periodicWorkRequest = PeriodicWorkRequestBuilder<LogWorker>(10, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
    }

    class LogWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
        override fun doWork(): Result {
            // 打印日志
            LogUtils.d("LogWorker", "doWork")
            return Result.success()
        }
    }
}