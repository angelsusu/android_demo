package com.example.test.rxjava

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.Common
import com.example.test.R
import com.example.test.commonDebug
import com.example.test.debug
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_rxjava_backpressure_test.*
import org.reactivestreams.Subscription


/**
 * author: beitingsu
 * created on: 2021/3/25 2:45 PM
 */
class RxJavaBackPressureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rxjava_backpressure_test)

        init()
    }

    private fun init() {
        btn_error_test?.setOnClickListener {
            errorTest()
        }
        btn_missing_test?.setOnClickListener {
            missingTest()
        }
        btn_buffer_test?.setOnClickListener {
            bufferTest()
        }
        btn_drop_test?.setOnClickListener {
            dropTest()
        }
        btn_latest_test?.setOnClickListener {
            latestTest()
        }
    }

    //需要在不同线程测试才有效, 缓存只有128大小，缓冲区只有异步的时候才需要
    @SuppressLint("CheckResult")
    private fun errorTest() {
        Flowable.create(FlowableOnSubscribe<Int> { emitter ->
            for (i in 0..Flowable.bufferSize() * 2) {
                emitter.onNext(i)
                commonDebug("flowable error emitter:$i")
            }
            emitter.onComplete()
        }, BackpressureStrategy.ERROR)
            .subscribeOn(Schedulers.io())  //必须要有
            .observeOn(AndroidSchedulers.mainThread()) //必须要有
            .subscribe({
                commonDebug("flowable error onNext:$it")
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {

                }
            }, {
                debug(Common.TAG, "flowable error onError:$it")
            }, {
                debug(Common.TAG, "flowable error onComplete")
            }, {
                it.request(Long.MAX_VALUE)
                debug(Common.TAG, "flowable error onSubscribe")
            })
    }

    @SuppressLint("CheckResult")
    private fun missingTest() {
        Flowable.create(FlowableOnSubscribe<Int> { emitter ->
            for (i in 0..Flowable.bufferSize() * 2) {
                emitter.onNext(i)
                commonDebug("flowable missing emitter:$i")
            }
            emitter.onComplete()
        }, BackpressureStrategy.MISSING)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                commonDebug("flowable missing onNext:$it")
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {

                }
            }, {
                debug(Common.TAG, "flowable missing onError:$it")
            }, {
                debug(Common.TAG, "flowable missing onComplete")
            }, {
                it.request(Long.MAX_VALUE)
                debug(Common.TAG, "flowable missing onSubscribe")
            })
    }

    @SuppressLint("CheckResult")
    private fun bufferTest() {
        Flowable.create(FlowableOnSubscribe<Int> { emitter ->
            for (i in 0..Flowable.bufferSize() * 2) {
                emitter.onNext(i)
                commonDebug("flowable buffer emitter:$i")
            }
            emitter.onComplete()
        }, BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                commonDebug("flowable buffer onNext:$it")
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {

                }
            }, {
                debug(Common.TAG, "flowable buffer onError:$it")
            }, {
                debug(Common.TAG, "flowable buffer onComplete")
            }, {
                it.request(Long.MAX_VALUE)
                debug(Common.TAG, "flowable buffer onSubscribe")
            })
    }

    @SuppressLint("CheckResult")
    private fun dropTest() {
        Flowable.create(FlowableOnSubscribe<Int> { emitter ->
            for (i in 0..Flowable.bufferSize() * 2) {
                emitter.onNext(i)
                commonDebug("flowable drop emitter:$i")
            }
            emitter.onComplete()
        }, BackpressureStrategy.DROP)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                commonDebug("flowable drop onNext:$it")
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {

                }
            }, {
                debug(Common.TAG, "flowable drop onError:$it")
            }, {
                debug(Common.TAG, "flowable drop onComplete")
            }, {
                it.request(Long.MAX_VALUE)
                debug(Common.TAG, "flowable drop onSubscribe")
            })
    }

    @SuppressLint("CheckResult")
    private fun latestTest() {
        Flowable.create(FlowableOnSubscribe<Int> { emitter ->
            for (i in 0..Flowable.bufferSize() * 2) {
                emitter.onNext(i)
                commonDebug("flowable latest emitter:$i")
            }
            emitter.onComplete()
        }, BackpressureStrategy.LATEST)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                commonDebug("flowable latest onNext:$it")
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {

                }
            }, {
                debug(Common.TAG, "flowable latest onError:$it")
            }, {
                debug(Common.TAG, "flowable latest onComplete")
            }, {
                it.request(Long.MAX_VALUE)
                debug(Common.TAG, "flowable latest onSubscribe")
            })
    }
}