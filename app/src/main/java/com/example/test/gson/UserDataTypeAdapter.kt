package com.example.test.gson

import com.example.test.data.UserData
import com.example.test.debug
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * author: beitingsu
 * created on: 2021/4/22 5:06 PM
 * 自定义json解析，适合整体解析
 */
class UserDataTypeAdapter : TypeAdapter<UserData>() {
    override fun write(out: JsonWriter?, value: UserData?) {
        debug("UserDataTypeAdapter", "write")
        out?.beginObject()
        out?.name("name")?.value(value?.name)
        out?.name("age")?.value(value?.age)
        out?.endObject()
    }

    override fun read(reader: JsonReader?): UserData {
        debug("UserDataTypeAdapter", "read")
        val user = UserData("default")
        reader?.beginObject()
        while (reader?.hasNext() == true) {
            when (reader.nextName()) {
                "name" -> {
                    user.name = reader.nextString()
                }
                "age" -> {
                    try {
                        val str: String = reader.nextString()
                        user.age = Integer.valueOf(str)
                    } catch (e: Exception) {

                    }
                }
            }
        }
        reader?.endObject()
        return user
    }
}

/**
 * Int类型处理器
 */
class IntegerTypeAdapter : TypeAdapter<Int>() {
    override fun write(out: JsonWriter?, value: Int?) {
        out?.value(value)
    }

    override fun read(reader: JsonReader?): Int {
        debug("IntegerTypeAdapter", "read")
        var result = 0
        try {
            val str = reader?.nextString()
            result = str?.toInt() ?: 0
        } catch (e: java.lang.Exception) {
        }
        return result
    }
}