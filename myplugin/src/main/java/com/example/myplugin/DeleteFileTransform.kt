package com.example.myplugin

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import java.io.File

/**
 * author: beitingsu
 * created on: 2021/12/8 6:39 下午
 */
class DeleteFileTransform : Transform() {
    override fun getName(): String {
        return "DeleteFileTransform"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        println("DeleteFileTransform")
        transformInvocation?.inputs?.forEach {
            it?.jarInputs?.forEach { input ->
                println(
                    "jar input:" + "path:" + input.file?.absolutePath
                            + ":name:" + input.file?.name
                            + ":scope:" + input.scopes
                )
                val dest = transformInvocation.outputProvider.getContentLocation(
                    input.name,
                    input.contentTypes, input.scopes, Format.JAR
                )
                FileUtils.copyFile(input.file, dest)
            }
            it.directoryInputs?.forEach { input ->
                println(
                    "dir input:" + "path:" + input.file?.absolutePath
                            + ":name:" + input.file?.name
                            + ":scope:" + input.scopes
                )
                //just test
                if (input?.file?.absolutePath == "/Users/beitingsu/MyProject/app/build/intermediates/javac/debug/classes") {
                    val file = File(input.file?.absolutePath + "/com/example/test/BuildConfig.class")
                    println("DeleteFile:" + file.absolutePath)
                    file.delete()
                }
                val dest = transformInvocation.outputProvider.getContentLocation(
                    input.name,
                    input.contentTypes, input.scopes, Format.DIRECTORY
                )
                FileUtils.copyDirectory(input.file, dest)
            }
        }
    }
}