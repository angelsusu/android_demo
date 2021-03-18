package com.example.test.dagger

import dagger.Component

/**
 * author: beitingsu
 * created on: 2021/3/16 3:27 PM
 */
@Component(modules = [AppModule::class])
interface AppComponent {

   //将实例返回出去，其他依赖的component才可以使用
   fun provideClassE() : ClassE
}