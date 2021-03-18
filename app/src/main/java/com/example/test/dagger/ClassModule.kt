package com.example.test.dagger

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * author: beitingsu
 * created on: 2021/3/16 4:36 PM
 */
@Module
class ClassModule {

    //@Named("param")
    @ClassQualifier("param")
    @Provides
    fun provideClassB(classA: ClassA): ClassB {
        return ClassB(classA)
    }

    //@Named("noParam")
    @ClassQualifier("noParam")
    @Provides
    fun provideClassBWithoutParam(): ClassB {
        return ClassB()
    }


    @Provides
    @Singleton
    fun provideClassC(): ClassC {
        return ClassC()
    }

    @Provides
    fun provideClassD(value: Int): ClassD {
        return ClassD(value)
    }
}