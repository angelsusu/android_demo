package com.example.test.data

import android.util.Log
import com.example.test.gson.IntegerTypeAdapter
import com.google.gson.annotations.JsonAdapter
import java.io.Serializable

/**
 * author: beitingsu
 * created on: 2021/4/22 11:02 AM
 */

/**
 * name没有默认值，用于测试gson反序列化问题
 */
data class UserData(
    var name: String,

    @JsonAdapter(IntegerTypeAdapter::class)
    var age: Int = 18
): Serializable

/**
 * 均带有默认值，用于测试gson反序列化问题
 */
data class UserDataWithDefault(val name: String = "", val age: Int = 18)



//--------------Test Lambda--------------//
//返回的是一个函数,调用方式为foo(x).invoke()或者foo(x)()
fun foo(age:Int) = run { Log.d("test", "$age") }

//本身就是函数，foo2(x)
fun foo2(age: Int) {
    Log.d("test", "$age")
}
