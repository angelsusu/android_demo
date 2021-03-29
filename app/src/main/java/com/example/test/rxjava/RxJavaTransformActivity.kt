package com.example.test.rxjava

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.Common
import com.example.test.R
import com.example.test.debug
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_rxjava_transform_test.*
import java.util.concurrent.TimeUnit


/**
 * author: beitingsu
 * created on: 2021/3/25 2:45 PM
 */
class RxJavaTransformActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rxjava_transform_test)

        init()
    }

    private fun init() {
        btn_map_test?.setOnClickListener {
            mapTest()
        }
        btn_flatMap_test?.setOnClickListener {
            flatMapTest()
        }
        btn_concatMap_test?.setOnClickListener {
            concatMapTest()
        }
        btn_buffer_test?.setOnClickListener {
            bufferTest()
        }
        btn_groupBy_test?.setOnClickListener {
            groupByTest()
        }
        btn_scan_test?.setOnClickListener {
            scanTest()
        }
        btn_window_test?.setOnClickListener {
            windowTest()
        }
    }

    @SuppressLint("CheckResult")
    private fun mapTest() {
        Observable.just(1, 2, 3)
            .map { t ->
                "I am $t"
            }
            .subscribe({
                debug(Common.TAG, "map onNext:$it")
            }, {
                debug(Common.TAG, "map onError:$it")
            }, {
                debug(Common.TAG, "map onComplete")
            }, {
                debug(Common.TAG, "map onSubscribe")
            })
    }

    @SuppressLint("CheckResult")
    private fun flatMapTest() {
        val students = arrayListOf(
            Student("11", arrayListOf(Course(1), Course(2))),
            Student("22", arrayListOf(Course(3), Course(4))),
            Student("33", arrayListOf(Course(5), Course(6)))
        )

        //map打印 课程id
        Observable.fromIterable(students)
            .map {
                it.courseList
            }.subscribe({ list ->
                //循环打印
                list?.forEach {
                    debug(Common.TAG, "map onNext:${it.id}")
                }
            }, {
                debug(Common.TAG, "map onError:$it")
            }, {
                debug(Common.TAG, "map onComplete")
            }, {
                debug(Common.TAG, "map onSubscribe")
            })

        //flatMap打印
        Observable.fromIterable(students)
            .flatMap { t ->
                Observable.fromIterable(t.courseList)
            }
            .subscribe({
                debug(Common.TAG, "flatMap onNext:${it.id}")
            }, {
                debug(Common.TAG, "flatMap onError:$it")
            }, {
                debug(Common.TAG, "flatMap onComplete")
            }, {
                debug(Common.TAG, "flatMap onSubscribe")
            })
    }

    @SuppressLint("CheckResult")
    private fun concatMapTest() {
        val students = arrayListOf(
            Student("11", arrayListOf(Course(1), Course(2))),
            Student("22", arrayListOf(Course(3), Course(4))),
            Student("33", arrayListOf(Course(5), Course(6)))
        )

        //flatMap
        Observable.fromIterable(students)
            .flatMap { t ->
                if (t.name == "11") {
                    Observable.fromIterable(t.courseList).delay(10, TimeUnit.MILLISECONDS)
                } else {
                    Observable.fromIterable(t.courseList)
                }
            }
            .subscribe({
                debug(Common.TAG, "flatMap onNext:${it.id}")
            }, {
                debug(Common.TAG, "flatMap onError:$it")
            }, {
                debug(Common.TAG, "flatMap onComplete")
            }, {
                debug(Common.TAG, "flatMap onSubscribe")
            })

        //concatMap
        Observable.fromIterable(students)
            .concatMap { t ->
                if (t.name == "11") {
                    Observable.fromIterable(t.courseList).delay(10, TimeUnit.MILLISECONDS)
                } else {
                    Observable.fromIterable(t.courseList)
                }
            }
            .subscribe({
                debug(Common.TAG, "concatMap onNext:${it.id}")
            }, {
                debug(Common.TAG, "concatMap onError:$it")
            }, {
                debug(Common.TAG, "concatMap onComplete")
            }, {
                debug(Common.TAG, "concatMap onSubscribe")
            })
    }

    @SuppressLint("CheckResult")
    private fun bufferTest() {
        Observable.just(1, 2, 3, 4, 5)
            .buffer(3, 4)
            .subscribe({ list ->
                list?.forEach {
                    debug(Common.TAG, "buffer onNext:$it")
                }
            }, {
                debug(Common.TAG, "buffer onError:$it")
            }, {
                debug(Common.TAG, "buffer onComplete")
            }, {
                debug(Common.TAG, "buffer onSubscribe")
            })
    }

    @SuppressLint("CheckResult")
    private fun groupByTest() {
        Observable.just(5, 2, 3, 4, 1, 6, 8, 9, 7, 10)
            .groupBy {
                it % 3
            }
            .subscribe({ group ->
                group.subscribe({
                    debug(Common.TAG, "groupBy inner onNext:group:${group.key}:value:${it}")
                }, {
                    debug(Common.TAG, "groupBy inner onError:$it")
                }, {
                    debug(Common.TAG, "groupBy inner onComplete")
                }, {
                    debug(Common.TAG, "groupBy inner onSubscribe")
                })
            }, {
                debug(Common.TAG, "groupBy onError:$it")
            }, {
                debug(Common.TAG, "groupBy onComplete")
            }, {
                debug(Common.TAG, "groupBy onSubscribe")
            })
    }

    @SuppressLint("CheckResult")
    private fun scanTest() {
        Observable.just(1, 2, 3, 4, 5)
            .scan { t1, t2 ->
                debug(Common.TAG, "scan:$t1:$t2")
                t1 + t2
            }
            .subscribe({
                debug(Common.TAG, "scan onNext:$it")
            }, {
                debug(Common.TAG, "scan onError:$it")
            }, {
                debug(Common.TAG, "scan onComplete")
            }, {
                debug(Common.TAG, "scan onSubscribe")
            })
    }

    @SuppressLint("CheckResult")
    private fun windowTest() {
        Observable.just(1, 2, 3, 4, 5)
            .window(2)
            .subscribe({ observable ->
                observable.subscribe({
                    debug(Common.TAG, "window inner onNext:$it")
                }, {
                    debug(Common.TAG, "window inner onError:$it")
                }, {
                    debug(Common.TAG, "window inner onComplete")
                }, {
                    debug(Common.TAG, "window inner onSubscribe")
                })
            }, {
                debug(Common.TAG, "window onError:$it")
            }, {
                debug(Common.TAG, "window onComplete")
            }, {
                debug(Common.TAG, "window onSubscribe")
            })
    }
}


class Student(var name: String = "", var courseList: List<Course>? = null)

class Course(var id: Int = 0)