package com.example.test.dagger

import javax.inject.Qualifier

/**
 * author: beitingsu
 * created on: 2021/3/16 5:25 PM
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ClassQualifier(val version: String = "") {

}