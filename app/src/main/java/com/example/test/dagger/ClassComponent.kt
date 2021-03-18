package com.example.test.dagger

import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * author: beitingsu
 * created on: 2021/3/16 3:27 PM
 */
@Singleton
@Component(dependencies = [AppComponent::class], modules = [ClassModule::class])
interface ClassComponent {
   fun inject(activity: DaggerActivity)

   @Component.Builder
   interface Builder {

      @BindsInstance
      fun value(value: Int): Builder

      fun appComponent(appComponent: AppComponent): Builder

      fun build(): ClassComponent
   }
}


