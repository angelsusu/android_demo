package com.example.test.coroutine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.test.Common
import com.example.test.R
import com.example.test.debug
import kotlinx.android.synthetic.main.activity_coroutine_test.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.suspendCoroutine


/**
 * author: beitingsu
 * created on: 2021/1/13 6:10 PM
 */
class CoroutineTestActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_test)

        init()
    }

    fun init() {
        lifecycleScope.launchWhenResumed {
            //debug(Common.TAG, "task launch when resumed")
        }

        //测试退出后 任务被取消
        btn_mainscope_test?.setOnClickListener {
            val job = launch {
                delay(3000)
                debug(Common.TAG, "task launch by MainScope")
            }

            //job 完成回调
            job.invokeOnCompletion { cause ->
                debug(Common.TAG, "job onCompletion:$cause")
            }
        }

        //suspendCoroutine测试
        btn_suspendCoroutine_test?.setOnClickListener {
            launch {
                val result = suspendCoroutineTest()
                debug(Common.TAG, "suspendCoroutineTest:$result")
            }
        }

        //join job，等待job完成
        btn_job_join_test?.setOnClickListener {
            val job = launch {
                delay(1000)
                debug(Common.TAG, "launch job join")
            }
            launch {
                debug(Common.TAG, "wait job complete-->begin")
                if (job.isCompleted || job.isCancelled) {
                    debug(Common.TAG, "wait job complete-->complete")
                } else {
                    job.join()
                    debug(Common.TAG, "wait job complete-->complete")
                }
            }
        }

        //async启动的任务 可以有返回结果 通过await可以获取
        btn_job_await_test?.setOnClickListener {
            val deferred = async {
                delay(2000)
                debug(Common.TAG, "async job")
                100
            }
            launch {
                debug(Common.TAG, "await job complete-->begin")
                val result = deferred.await()
                debug(Common.TAG, "await job complete-->complete:$result")
            }
        }

        //协程异常处理
        btn_exception_test?.setOnClickListener {
            exceptionHandlerTest()
        }
        btn_exception_launch_test?.setOnClickListener {
            exceptionLaunchTest()
        }
        btn_exception_async_test?.setOnClickListener {
            exceptionAsyncTest()
        }
        btn_exception_double_test?.setOnClickListener {
            exceptionTestGlobalScope()
        }
        btn_exception_singel_test?.setOnClickListener {
            exceptionTestSupervisorScope()
        }
    }

    //callback转suspend
    private suspend fun suspendCoroutineTest() =
        suspendCoroutine<Boolean> { continuation ->
            callbackFun(success = {
                continuation.resumeWith(Result.success(true))
            }, fail = {
                continuation.resumeWith(Result.success(false))
            })
        }

    private fun callbackFun(success: (() -> Unit), fail: (() -> Unit)) {
        val random = Random().nextInt(10)
        if (random < 5) {
            success.invoke()
        } else {
            fail.invoke()
        }
    }

    private fun exceptionHandlerTest() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            debug(Common.TAG, "throws an exception with message:${throwable.message}")
        }
        launch(exceptionHandler) {
            throw RuntimeException("test exception")
        }
    }

    private fun exceptionLaunchTest() {
        //外部try catch无效，launch不传递异常, 异常发生在本协程所在的线程
        GlobalScope.launch {
            try {
                throw RuntimeException("test exception exceptionLaunchTest")
            } catch (e: Exception) {
                debug(Common.TAG, "exceptionLaunchTest catch exception:$e")
            }
        }
    }


    private fun exceptionAsyncTest() {
        launch {
            //外部try catch, async会向外传播异常
            try {
                val deferred = GlobalScope.async {
                    throw RuntimeException("test exception exceptionAsyncTest")
                    true
                }
                deferred.await()
            } catch (e: Exception) {
                debug(Common.TAG, "exceptionAsyncTest catch exception:$e")
            }
        }
    }

    //默认作用域，取消是双向的
    private fun exceptionTestGlobalScope() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            debug(Common.TAG, "throws an exception with message:${throwable.message}")
        }

        //(1) 顶级作用域
        val job = Job()
        val scope = CoroutineScope(job + Dispatchers.Default + exceptionHandler)
        val job1 = scope.launch {
            debug(Common.TAG, "exceptionTestGlobalScope launch 1 start")
            delay(500)
            debug(Common.TAG, "exceptionTestGlobalScope launch 1 end")
        }
        val job2 = scope.launch {
            debug(Common.TAG, "exceptionTestGlobalScope launch 2 start")
            throw RuntimeException("test hello!")
        }
        launch {
            delay(1000)
            debug(Common.TAG, "exceptionTestGlobalScope:${job1.isCancelled}:${job2.isCancelled}")
        }

    }

    //supervisorScope，取消是单向的
    private fun exceptionTestSupervisorScope() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            debug(Common.TAG, "throws an exception with message:${throwable.message}")
        }

        //(1) 顶级作用域
        val job = SupervisorJob()
        val scope = CoroutineScope(job + Dispatchers.Default + exceptionHandler)
        val job1 = scope.launch {
            debug(Common.TAG, "exceptionTestSupervisorScope launch 1 start")
            delay(500)
            debug(Common.TAG, "exceptionTestSupervisorScope launch 1 end")
        }
        val job2 = scope.launch {
            debug(Common.TAG, "exceptionTestSupervisorScope launch 2 start")
            throw RuntimeException("test hello!")
        }
        launch {
            delay(1000)
            debug(Common.TAG, "exceptionTestGlobalScope:${job1.isCancelled}:${job2.isCancelled}")
        }
    }

    override fun onResume() {
        super.onResume()
        //debug(Common.TAG, "CoroutineTestActivity onResume")
    }

    override fun onDestroy() {
        super.onDestroy()

        //取消所有协程任务
        cancel()
    }


    object MyCoroutineScope : CoroutineScope {
        override val coroutineContext: CoroutineContext = buildContext()

        private fun buildContext(): CoroutineContext {
            return SupervisorJob() + Dispatchers.Main
        }
    }

}