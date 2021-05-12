package com.example.test.dagger

import dagger.Component

/**
 * author: beitingsu
 * created on: 2021/3/16 3:27 PM
 */
@Component(modules = [MVPModule::class])
interface MVPComponent {
   fun inject(activity: DaggerActivity)
}


