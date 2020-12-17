package com.example.phototest

import android.util.Log

/**
 * author: beitingsu
 * created on: 2020/5/11 5:17 PM
 */
class Logger {

    companion object {
        private var mLogImp : ILogService?= null
        private var mIsDebug = false
        private var mLogLevel = LogLevel.LOG_LEVEL_DEBUG

        @JvmStatic
        fun initLog() {
            mLogImp?.initLog()
        }

        /**
         * 设置日志实例
         */
        @JvmStatic
        fun setLogImp(logImp : ILogService) {
            mLogImp = logImp
        }

        /**
         * 设置是否debug，控制是否打印logcat
         */
        @JvmStatic
        fun setDebug(isDebug: Boolean) {
            mIsDebug = isDebug
            mLogImp?.setDebug(isDebug)
        }

        /**
         * 设置日志级别，控制哪些级别日志需要输出到Logan
         */
        @JvmStatic
        fun setLogLevel(logLevel : Int) {
            mLogLevel = logLevel
        }

        @JvmStatic
        fun logV(tag: String, msg: String) {
            if (mIsDebug) {
                Log.v(tag, msg)
            }
            if (checkLogLevel(LogLevel.LOG_LEVEL_VERBOSE)) {
                mLogImp?.logV(tag, msg)
            }
        }

        @JvmStatic
        fun logD(tag: String, msg: String) {
            if (mIsDebug) {
                Log.d(tag, msg)
            }
            if (checkLogLevel(LogLevel.LOG_LEVEL_DEBUG)) {
                mLogImp?.logD(tag, msg)
            }
        }

        @JvmStatic
        fun logI(tag: String, msg: String) {
            if (mIsDebug) {
                Log.i(tag, msg)
            }
            if (checkLogLevel(LogLevel.LOG_LEVEL_INFO)) {
                mLogImp?.logI(tag, msg)
            }
        }

        @JvmStatic
        fun logW(tag: String, msg: String) {
            if (mIsDebug) {
                Log.w(tag, msg)
            }
            if (checkLogLevel(LogLevel.LOG_LEVEL_WARNING)) {
                mLogImp?.logW(tag, msg)
            }
        }

        @JvmStatic
        fun logE(tag: String, msg: String) {
            if (mIsDebug) {
                Log.e(tag, msg)
            }
            if (checkLogLevel(LogLevel.LOG_LEVEL_ERROR)) {
                mLogImp?.logE(tag, msg)
            }
        }

        @JvmStatic
        fun logReport(url: String, date: String) {
            mLogImp?.logReport(url, date)
        }

        @JvmStatic
        fun checkLogLevel(logLevel: Int) : Boolean {
            return logLevel >= mLogLevel
        }
    }
}