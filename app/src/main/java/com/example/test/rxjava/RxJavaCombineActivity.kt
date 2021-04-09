package com.example.test.rxjava

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.Common
import com.example.test.R
import com.example.test.debug
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_rxjava_combine_test.*
import java.util.concurrent.TimeUnit


/**
 * author: beitingsu
 * created on: 2021/3/25 2:45 PM
 */
class RxJavaCombineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rxjava_combine_test)

        init()
    }

    private fun init() {
        btn_concat_test?.setOnClickListener {
            concatTest()
        }
        btn_concatArray_test?.setOnClickListener {
            concatArrayTest()
        }
        btn_merge_test?.setOnClickListener {
            mergeTest()
        }
    }

    @SuppressLint("CheckResult")
    private fun concatTest() {
        Observable.concat(
            Observable.just(1, 2),
            Observable.just(3, 4),
            Observable.just(5, 6),
            Observable.just(7, 8)
        ).subscribe({
            debug(Common.TAG, "concat onNext:$it")
        }, {
            debug(Common.TAG, "concat onError:$it")
        }, {
            debug(Common.TAG, "concat onComplete")
        }, {
            debug(Common.TAG, "concat onSubscribe")
        })
    }

    @SuppressLint("CheckResult")
    private fun concatArrayTest() {
        Observable.concatArray(
            Observable.just(1, 2),
            Observable.just(3, 4),
            Observable.just(5, 6),
            Observable.just(7, 8),
            Observable.just(9, 10)
        ).subscribe({
            debug(Common.TAG, "concatArray onNext:$it")
        }, {
            debug(Common.TAG, "concatArray onError:$it")
        }, {
            debug(Common.TAG, "concatArray onComplete")
        }, {
            debug(Common.TAG, "concatArray onSubscribe")
        })
    }

    @SuppressLint("CheckResult")
    private fun mergeTest() {
        Observable.merge(
            Observable.interval(1, TimeUnit.SECONDS).map {
                "A$it"
            },
            Observable.interval(1, TimeUnit.SECONDS).map {
                "B$it"
            }
        ).subscribe({
            debug(Common.TAG, "merge onNext:$it")
        }, {
            debug(Common.TAG, "merge onError:$it")
        }, {
            debug(Common.TAG, "merge onComplete")
        }, {
            debug(Common.TAG, "merge onSubscribe")
        })


        Observable.concat(
            Observable.interval(1, TimeUnit.SECONDS).map {
                "A$it"
            },
            Observable.interval(1, TimeUnit.SECONDS).map {
                "B$it"
            }
        ).subscribe({
            debug(Common.TAG, "concat onNext:$it")
        }, {
            debug(Common.TAG, "concat onError:$it")
        }, {
            debug(Common.TAG, "concat onComplete")
        }, {
            debug(Common.TAG, "concat onSubscribe")
        })
    }

}