package com.android.jetpack.workmanager

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

/**
 * WorkManager 用于执行异步任务，即使应用退出或者设备重启。
 * 主要功能
 * 1最高向后兼容到 API 14
 *  在运行 API 23 及以上级别的设备上使用 JobScheduler
 *  在运行 API 14-22 的设备上结合使用 BroadcastReceiver 和 AlarmManager
 * 2添加网络可用性或充电状态等工作约束
 * 3调度一次性或周期性异步任务
 * 4监控和管理计划任务
 * 5将任务链接起来
 * 6确保任务执行，即使应用或设备重启也同样执行任务
 * 7遵循低电耗模式等省电功能
 *
 *查看basiclib的build.gradle文件查看如何导入
 * 同时需要添加
 * compileOptions {
 *    sourceCompatibility JavaVersion.VERSION_1_8
 *    targetCompatibility JavaVersion.VERSION_1_8
 * }
 * kotlinOptions {
 *   jvmTarget = JavaVersion.VERSION_1_8.toString()
 * }
 *
 */

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Worker 定义工作单元，WorkRequest 则定义工作的运行方式和时间。
        // 任务可以是一次性的，也可以是周期性的。
        // 对于一次性 WorkRequest，请使用 OneTimeWorkRequest，
        // 对于周期性工作，请使用 PeriodicWorkRequest。
        btnCreateMission.setOnClickListener {
            //创建一个最简单的任务
            val testWorker = OneTimeWorkRequestBuilder<TestWorker>().build()
            //提交任务
            WorkManager.getInstance(this).enqueue(testWorker)
        }

        btnCreateConstraintsMission.setOnClickListener {
            val constraints = Constraints.Builder()
                .setRequiresCharging(true)//设备是否在充电
                .setRequiresDeviceIdle(false)//设备是否处于空闲状态
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val testWorker = OneTimeWorkRequestBuilder<TestWorker>()
                .setConstraints(constraints).build()
            WorkManager.getInstance(this).enqueue(testWorker)
        }

        btnCreateDelayMission.setOnClickListener {
            val testWorker = OneTimeWorkRequestBuilder<TestWorker>()
                .setInitialDelay(5, TimeUnit.SECONDS).build()
            WorkManager.getInstance(this).enqueue(testWorker)
        }

        btnCreateInOutMission.setOnClickListener {
            val testWorker = OneTimeWorkRequestBuilder<TestWorker1>()
                .setInputData(workDataOf("key" to "value")).build()
            /**
             * 工作状态及输出数据均保存在workinfo 中，以下是打印结果
             *WorkInfo{mId='6f382725-52d6-4328-80ed-6e13394ac37e', mState=ENQUEUED, mOutputData=Data {}, mTags=[com.android.jetpack.workmanager.MainActivityTestWorker1], mProgress=Data {}}
             *WorkInfo{mId='6f382725-52d6-4328-80ed-6e13394ac37e', mState=RUNNING, mOutputData=Data {}, mTags=[com.android.jetpack.workmanager.MainActivityTestWorker1], mProgress=Data {}}
             *WorkInfo{mId='6f382725-52d6-4328-80ed-6e13394ac37e', mState=SUCCEEDED, mOutputData=Data {key1 : value1, }, mTags=[com.android.jetpack.workmanager.MainActivityTestWorker1], mProgress=Data {}}
             */
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(testWorker.id).observe(this,
                Observer { workInfo ->
                    Log.i(TAG, workInfo.toString())
                })
            WorkManager.getInstance(this).enqueue(testWorker)

        }
        btnCreateProcessMission.setOnClickListener {
            val testWorker = OneTimeWorkRequestBuilder<TestWorker2>().build()
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(testWorker.id).observe(this,
                Observer { workInfo ->
                    workInfo?.let {
                        if (it.state != WorkInfo.State.SUCCEEDED) {
                            var process = it.progress.getInt("Process", 0)
                            Log.i(TAG, process.toString())
                        }
                    }

                })
            WorkManager.getInstance(this).enqueue(testWorker)
        }
        btnCreateChainMission.setOnClickListener {
            val testWorker = OneTimeWorkRequestBuilder<TestWorker>().build()

            /**
             * 上一级的输出会给下一级的输入，通过设置setInputMerger 来确定输入参数以怎样的方式合并
             * WorkManager 提供两种不同类型的 InputMerger：
             *     OverwritingInputMerger 会尝试将所有输入中的所有键添加到输出中。如果发生冲突，它会覆盖先前设置的键。
             *     ArrayCreatingInputMerger 会尝试合并输入，并在必要时创建数组。
             *
             *  一个典型的应用场景：注册之后立即登录
             */
            val testWorker1 = OneTimeWorkRequestBuilder<TestWorker1>()
                .setInputMerger(ArrayCreatingInputMerger::class)
                .build()
            val testWorker2 = OneTimeWorkRequestBuilder<TestWorker2>()
                .setInputMerger(OverwritingInputMerger::class).build()
            WorkManager.getInstance(this).beginWith(testWorker).then(testWorker1).then(testWorker2)
                .enqueue()
        }

        btnCreatePeriodicMission.setOnClickListener {
            /**
             * 需要注意的是，可以定义的最短重复间隔为15分钟，小于15分钟的应用场景此方式并不适合
             */
            var testWorker = PeriodicWorkRequestBuilder<TestWorker>(15, TimeUnit.MINUTES).build()
            WorkManager.getInstance(this).enqueue(testWorker)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        WorkManager.getInstance(this).cancelAllWork()//取消所有任务，也可以通过Id或者Tag的方式取消单个任务
    }


    /**
     * 创建一个任务
     * 这里需要注意，此类只能是嵌套类，不能是内部类（即不能用inner 修饰），因为Worker是通过反射创建的，如果是内部类会返回以下错误
     * WM-WorkerFactory: Could not instantiate com.android.jetpack.workmanager
     * java.lang.NoSuchMethodException: <init> [class android.content.Context, class androidx.work.WorkerParameters]
     */
    class TestWorker(appContext: Context, workerParams: WorkerParameters) :
        Worker(appContext, workerParams) {
        override fun doWork(): Result {
            //doWork 是在异步线程中执行的，不是主线程
            applicationContext.runOnUiThread {
                toast("doWork接收到任务")
            }
            return Result.success()//已成功完成：Result.success() //已失败：Result.failure()//稍后重试：Result.retry()
        }
    }

    class TestWorker1(appContext: Context, workerParams: WorkerParameters) :
        Worker(appContext, workerParams) {
        override fun doWork(): Result {
            applicationContext.runOnUiThread {
                toast("doWork接收到任务,参数:${inputData.getString("key")}")
            }
            return Result.success(workDataOf("key1" to "value1"))//返回结果在WorkInfo 中
        }
    }

    class TestWorker2(appContext: Context, workerParams: WorkerParameters) :
        CoroutineWorker(appContext, workerParams) {
        override suspend fun doWork(): Result {
            setProgress(workDataOf("Process" to 50))
            delay(1000L)
            setProgress(workDataOf("Process" to 100))
            delay(1000L)//如果去掉这个延时 process 100 并没有回调，还没找到原因
            return Result.success()//返回结果在WorkInfo 中
        }
    }
}
