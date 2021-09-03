package com.example.test.jacoco

/**
 * author: beitingsu
 * created on: 2021/7/12 2:21 下午
 */
interface FinishListener {
    fun onActivityFinished()
    fun dumpIntermediateCoverage(filePath: String?)
}