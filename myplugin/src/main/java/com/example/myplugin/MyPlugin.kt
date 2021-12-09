package com.example.myplugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * author: beitingsu
 * created on: 2021/12/8 6:34 下午
 */
class MyPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("MyPlugin apply")
        val extension = target.extensions.getByType(AppExtension::class.java)
        extension.registerTransform(DeleteFileTransform())
    }
}