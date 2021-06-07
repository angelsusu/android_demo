package com.example.test.expand

import com.example.test.commonDebug

/**
 * author: beitingsu
 * created on: 2021/6/7 5:26 下午
 */
open class Parent {
    var param = "init"
    fun print() = run {
        commonDebug("print in Parent")
    }
}

class Child : Parent()

fun Parent.test() = run {
    commonDebug("expand in Parent")
}

//定义和父类一样的函数
fun Child.test() = run {
    commonDebug("expand in Child")
}

//扩展方法和成员方法定义一致，不会调用该方法，成员方法优先
fun Parent.print() = run {
    commonDebug("this is defined same print fun")
}

//定义扩展属性
var Parent.expandParam: String
    set(value) {
        param = "$param:$value"
    }
    get() = param
