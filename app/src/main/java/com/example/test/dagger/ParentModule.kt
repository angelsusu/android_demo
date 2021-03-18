package com.example.test.dagger

import dagger.Module
import dagger.Provides

/**
 * author: beitingsu
 * created on: 2021/3/18 4:44 PM
 */
@Module
class ParentModule {

    @Provides
    fun provideClassG() : ClassG {
        return ClassG()
    }
}