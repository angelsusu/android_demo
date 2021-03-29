package com.example.test.rxjava

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.Common
import com.example.test.R
import com.example.test.debug
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_rxjava_create_test.*
import kotlinx.android.synthetic.main.activity_rxjava_test.btn_create_test
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit


/**
 * author: beitingsu
 * created on: 2021/3/25 2:45 PM
 */
class RxJavaCreateActivity : AppCompatActivity() {

    private val mDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rxjava_create_test)

        init()
    }

    private fun init() {
        btn_create_test?.setOnClickListener {
            baseTest()
        }
        btn_just_test?.setOnClickListener {
            justTest()
        }
        btn_timer_test?.setOnClickListener {
            timerTest()
        }
        btn_interval_test?.setOnClickListener {
            intervalTest()
        }
        btn_interval_range_test?.setOnClickListener {
            intervalRangeTest()
        }
        btn_range_test?.setOnClickListener {
            rangeTest()
        }
        btn_empty_test?.setOnClickListener {
            emptyTest()
        }
        btn_defer_test?.setOnClickListener {
            deferTest()
        }
        btn_fromArray_test?.setOnClickListener {
            fromArrayTest()
        }
        btn_fromCallable_test?.setOnClickListener {
            fromCallableTest()
        }
        btn_fromFuture_test?.setOnClickListener {
            fromFutureTest()
        }
        btn_fromIterable_test?.setOnClickListener {
            fromIterableTest()
        }
    }

    private fun baseTest() {
        var dispose: Disposable? = null
        // 创建被观察者
        val observable = Observable.create(ObservableOnSubscribe<String?> { emitter ->
            emitter.onNext("hello")
            emitter.onNext("i love china")
            emitter.onNext("i love love")
            emitter.onError(Throwable("error"))
            //emitter.onComplete()
        })

        // 创建观察者
        val observer = object : Observer<String?> {
            override fun onComplete() {
                debug(Common.TAG, "onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                dispose = d
                debug(Common.TAG, "onSubscribe")
            }

            override fun onNext(t: String) {
                debug(Common.TAG, "onNext:$t")
            }

            override fun onError(e: Throwable) {
                debug(Common.TAG, "onError:$e")
            }
        }

        // 订阅
        observable.subscribe(observer)

        if (dispose?.isDisposed == false) {
            //取消订阅
            debug(Common.TAG, "dispose")
            dispose?.dispose()
        }
    }

    @SuppressLint("CheckResult")
    private fun justTest() {
        Observable.just("hello", "ha ha").subscribe({
            debug(Common.TAG, "just onNext:$it")
        }, {
            debug(Common.TAG, "just onError:$it")
        }, {
            debug(Common.TAG, "just onComplete")
        }, {
            debug(Common.TAG, "just onSubscribe")
        })
    }


    @SuppressLint("CheckResult")
    private fun timerTest() {
        Observable.timer(2, TimeUnit.SECONDS).subscribe({
            debug(Common.TAG, "timer onNext:$it")
        }, {
            debug(Common.TAG, "timer onError:$it")
        }, {
            debug(Common.TAG, "timer onComplete")
        }, {
            debug(Common.TAG, "timer onSubscribe")
        })
    }

    @SuppressLint("CheckResult")
    private fun intervalTest() {
        Observable.interval(1, TimeUnit.SECONDS).subscribe({
            debug(Common.TAG, "interval onNext:$it")
        }, {
            debug(Common.TAG, "interval onError:$it")
        }, {
            debug(Common.TAG, "interval onComplete")
        }, {
            mDisposable.add(it)
            debug(Common.TAG, "interval onSubscribe")
        })
    }

    @SuppressLint("CheckResult")
    private fun intervalRangeTest() {
        Observable.intervalRange(1, 5, 1, 1, TimeUnit.SECONDS).subscribe({
            debug(Common.TAG, "intervalRange onNext:$it")
        }, {
            debug(Common.TAG, "intervalRange onError:$it")
        }, {
            debug(Common.TAG, "intervalRange onComplete")
        }, {
            mDisposable.add(it)
            debug(Common.TAG, "intervalRange onSubscribe")
        })
    }

    @SuppressLint("CheckResult")
    private fun rangeTest() {
        Observable.range(1, 6).subscribe({
            debug(Common.TAG, "range onNext:$it")
        }, {
            debug(Common.TAG, "range onError:$it")
        }, {
            debug(Common.TAG, "range onComplete")
        }, {
            mDisposable.add(it)
            debug(Common.TAG, "range onSubscribe")
        })
    }

    @SuppressLint("CheckResult")
    private fun emptyTest() {
        //empty
        Observable.empty<Any>().subscribe({
            debug(Common.TAG, "empty onNext:$it")
        }, {
            debug(Common.TAG, "empty onError:$it")
        }, {
            debug(Common.TAG, "empty onComplete")
        }, {
            mDisposable.add(it)
            debug(Common.TAG, "empty onSubscribe")
        })

        //never
        Observable.never<Any>().subscribe({
            debug(Common.TAG, "never onNext:$it")
        }, {
            debug(Common.TAG, "never onError:$it")
        }, {
            debug(Common.TAG, "never onComplete")
        }, {
            mDisposable.add(it)
            debug(Common.TAG, "never onSubscribe")
        })

        //error
        Observable.error<Throwable>(RuntimeException("error")).subscribe({
            debug(Common.TAG, "error onNext:$it")
        }, {
            debug(Common.TAG, "error onError:$it")
        }, {
            debug(Common.TAG, "error onComplete")
        }, {
            mDisposable.add(it)
            debug(Common.TAG, "error onSubscribe")
        })
    }

    private fun deferTest() {
        var i = 100

        val observable = Observable.defer<Int> {
            Observable.just<Int>(i)
        }

        i = 200

        // 创建观察者
        val observer = object : Observer<Int> {
            override fun onComplete() {
                debug(Common.TAG, "defer onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                debug(Common.TAG, "defer onSubscribe")
            }

            override fun onNext(t: Int) {
                debug(Common.TAG, "defer onNext:$t")
            }

            override fun onError(e: Throwable) {
                debug(Common.TAG, "defer onError:$e")
            }
        }

        observable.subscribe(observer)

        i = 300

        observable.subscribe(observer)
    }

    @SuppressLint("CheckResult")
    private fun fromArrayTest() {
        val array = arrayOf(1, 2, 3, 4)
        Observable.fromArray(*array).subscribe({
            debug(Common.TAG, "fromArray onNext:$it")
        }, {
            debug(Common.TAG, "fromArray onError:$it")
        }, {
            debug(Common.TAG, "fromArray onComplete")
        }, {
            debug(Common.TAG, "fromArray onSubscribe")
        })
    }

    @SuppressLint("CheckResult")
    private fun fromCallableTest() {
        Observable.fromCallable { 1 }.subscribe({
            debug(Common.TAG, "fromCallable onNext:$it")
        }, {
            debug(Common.TAG, "fromCallable onError:$it")
        }, {
            debug(Common.TAG, "fromCallable onComplete")
        }, {
            debug(Common.TAG, "fromCallable onSubscribe")
        })
    }


    @SuppressLint("CheckResult")
    private fun fromFutureTest() {
        val futureTask = FutureTask(Callable<String> { "hello" })
        Observable.fromFuture(futureTask).doOnSubscribe {
            futureTask.run()
        }.subscribe({
            debug(Common.TAG, "fromFuture onNext:$it")
        }, {
            debug(Common.TAG, "fromFuture onError:$it")
        }, {
            debug(Common.TAG, "fromFuture onComplete")
        }, {
            debug(Common.TAG, "fromFuture onSubscribe")
        })
    }

    @SuppressLint("CheckResult")
    private fun fromIterableTest() {
        Observable.fromIterable(arrayListOf(1, 2, 3, 4, 5)).subscribe({
            debug(Common.TAG, "fromIterable onNext:$it")
        }, {
            debug(Common.TAG, "fromIterable onError:$it")
        }, {
            debug(Common.TAG, "fromIterable onComplete")
        }, {
            debug(Common.TAG, "fromIterable onSubscribe")
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        mDisposable.dispose()
    }
}