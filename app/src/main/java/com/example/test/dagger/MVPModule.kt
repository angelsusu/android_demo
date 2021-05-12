package com.example.test.dagger

import dagger.Module
import dagger.Provides

/**
 * author: beitingsu
 * created on: 2021/3/16 4:36 PM
 */
@Module
class MVPModule(private val view: IMVPView) {

    @Provides
    fun provideView(): IMVPView {
        return view
    }
}