package com.example.test.multidex

import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * author: beitingsu
 * created on: 2021/11/25 3:33 下午
 * 反射工具类，用来获取某个字段或者方法
 */
object ReflectUtil {

    /**
     * 获取某个字段
     * @param instance 对象实例
     * @param name 字段名称
     */
    @Throws(NoSuchFieldException::class)
    fun findFiled(instance: Any, name: String): Field {
        var clazz: Class<Any>? = instance.javaClass
        while (clazz != null) {
            try {
                val filed = clazz.getDeclaredField(name)
                if (!filed.isAccessible) {
                    filed.isAccessible = true
                }
                return filed
            } catch (ex: NoSuchFieldException) {
                clazz = clazz.superclass as Class<Any>?
            }
        }
        throw NoSuchFieldException("no such filed: $name")
    }

    /**
     * 获取某个方法
     * @param instance 对象实例
     * @param name 方法名称
     * @param paramTypes 参数类型列表
     */
    @Throws(NoSuchMethodException::class)
    fun findMethod(instance: Any, name: String, vararg paramTypes: Class<*>): Method {
        var clazz: Class<Any>? = instance.javaClass
        while (clazz != null) {
            try {
                val method = clazz.getDeclaredMethod(name, *paramTypes)
                if (!method.isAccessible) {
                    method.isAccessible = true
                }
                return method
            } catch (ex: NoSuchMethodException) {
                clazz = clazz.superclass as Class<Any>?
            }
        }
        throw NoSuchMethodException("no such method: $name")
    }
}