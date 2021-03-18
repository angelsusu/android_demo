package com.example.test.dagger

import dagger.Component

/**
 * author: beitingsu
 * created on: 2021/3/16 3:27 PM
 */
@Component(modules = [ParentModule::class])
interface ParentComponent {

   //返回子组件
   fun subClassComponent(): SubClassComponent
}