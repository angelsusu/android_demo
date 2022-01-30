package com.example.test

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.test.multidex.HotFixManager

/**
 * author: beitingsu
 * created on: 2020/12/17 3:10 PM
 */
class MyApplication: MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        MultiDex.install(this)
        HotFixManager.installFixedDex(this)
    }
}