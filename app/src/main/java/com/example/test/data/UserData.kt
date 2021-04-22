package com.example.test.data

import com.example.test.gson.IntegerTypeAdapter
import com.google.gson.annotations.JsonAdapter

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
)

/**
 * 均带有默认值，用于测试gson反序列化问题
 */
data class UserDataWithDefault(val name: String = "", val age: Int = 18)