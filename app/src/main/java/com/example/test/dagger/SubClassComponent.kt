package com.example.test.dagger

import dagger.Subcomponent
import javax.inject.Singleton

/**
 * author: beitingsu
 * created on: 2021/3/16 3:27 PM
 */
@Subcomponent(modules = [SubClassModule::class])
interface SubClassComponent {
   fun inject(activity: DaggerActivity)
}


