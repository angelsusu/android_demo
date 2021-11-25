package com.example.test.multidex

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.IOException
import java.lang.reflect.Array
import java.util.*

/**
 * author: beitingsu
 * created on: 2021/11/25 4:03 下午
 * 运行时注入补丁包
 */
object HotFixManager {

    //正常逻辑可以将下载的文件放在应用内部目录，就不需要申请权限了
    private val FIX_DEX_NAME = Environment.getExternalStorageDirectory().path + "/fixed.dex"

    /**
     * 注入补丁包
     */
    fun installFixedDex(context: Context) {
        kotlin.runCatching {
            val fixedFile = File(FIX_DEX_NAME)
            //补丁包不存在 不需要处理
            if (!fixedFile.exists()) {
                return
            }

            //获取dexPathList字段
            val pathListFiled = ReflectUtil.findFiled(context.classLoader, "pathList")
            val dexPathList = pathListFiled.get(context.classLoader)

            //获取dexPathList中的makeDexElements方法
            val makeDexElements = ReflectUtil.findMethod(
                dexPathList,
                "makeDexElements",
                List::class.java,
                File::class.java,
                List::class.java,
                ClassLoader::class.java
            )

            //调用makeDexElements方法，得到待修复dex文件对应的Element数组
            //需要对着不同版本的源码来写
            val toBeFixedElements = makeDexElements.invoke(
                dexPathList, arrayListOf(fixedFile),
                File(context.filesDir, "fixed_dex"),
                arrayListOf<IOException>(),
                context.classLoader
            ) as kotlin.Array<*>

            //获取原始的Element数组
            val dexElements = ReflectUtil.findFiled(dexPathList, "dexElements")
            val originElements = dexElements.get(dexPathList) as kotlin.Array<*>

            //创建一个新的Elements数组,合并两个数据, 需要先将待修复的数据放入前面，
            //确保先从toBeFixedElements中查找，达到修复的效果
            val combinedElements = Array.newInstance(
                originElements.javaClass.componentType,
                originElements.size + toBeFixedElements.size
            )
            System.arraycopy(toBeFixedElements, 0, combinedElements, 0, toBeFixedElements.size)
            System.arraycopy(
                originElements,
                0,
                combinedElements,
                toBeFixedElements.size,
                originElements.size
            )

            //替换原有的dexPathList的dexElements
            dexElements.set(dexPathList, combinedElements)
        }
    }

}