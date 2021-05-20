package com.example.test.rxjava

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

/**
 * author: beitingsu
 * created on: 2021/5/20 11:04 AM
 */
data class Event(val topic: Int, val data: Any)

object Bus {

    @JvmStatic
    fun push(event: Event) {
        RxBus.push(event)
    }

    @JvmStatic
    fun monitorTopicEventOnUI(topic: Int): Observable<Event> {
        return RxBus.monitorTopicEventOnUI(Event::class.java).filter { event ->
            event.topic == topic
        }
    }

    @JvmStatic
    fun monitorTopicEventBackground(topic: Int): Observable<Event> {
        return RxBus.monitorTopicEventBackground(Event::class.java).filter { event ->
            event.topic == topic
        }
    }
}

object RxBus {
    private val sStream: Subject<Any> = PublishSubject.create<Any>().toSerialized()

    @JvmStatic
    fun push(event: Any) {
        sStream.onNext(event)
    }

    @JvmStatic
    fun <T> monitorTopicEventOnUI(clazz: Class<T>): Observable<T> {
        return sStream.ofType(clazz).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    @JvmStatic
    fun <T> monitorTopicEventBackground(clazz: Class<T>): Observable<T> {
        return sStream.ofType(clazz).subscribeOn(Schedulers.io())
    }
}