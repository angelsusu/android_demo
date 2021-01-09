package com.example.test

import android.util.Log

/**
 * author: beitingsu
 * created on: 2021/1/9 10:36 AM
 */

//日志打印
inline fun <reified T> T.debug(log: Any) {
    Log.d(T::class.simpleName, log.toString())
}

//日志打印
inline fun <reified T> T.debug(tag:String, log: Any) {
    Log.d(tag, log.toString())
}