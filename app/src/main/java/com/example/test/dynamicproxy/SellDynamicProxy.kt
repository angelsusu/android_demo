package com.example.test.dynamicproxy

import com.example.test.Common
import com.example.test.debug
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * author: beitingsu
 * created on: 2021/4/16 3:56 PM
 * 动态代理类
 */
class SellDynamicProxy(private val obj: Any? = null) : InvocationHandler {


    //当我们调用代理类对象的方法时，最终会调用到invoke方法
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        debug(Common.TAG, "DynamicProxy invoke before")
        val result = method?.invoke(obj)
        debug(Common.TAG, "DynamicProxy invoke after")
        return result
    }
}