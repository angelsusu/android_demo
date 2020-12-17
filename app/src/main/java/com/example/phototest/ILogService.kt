package com.example.phototest

/**
 * author: beitingsu
 * created on: 2020/5/11 4:50 PM
 */
interface ILogService {

    fun initLog()

    fun logV(tag: String, msg: String)

    fun logD(tag: String, msg: String)

    fun logI(tag: String, msg: String)

    fun logW(tag: String, msg: String)

    fun logE(tag: String, msg: String)

    fun setDebug(isDebug: Boolean)

    /**
     * 上报服务端
     */
    fun logReport(url: String, date: String)
}