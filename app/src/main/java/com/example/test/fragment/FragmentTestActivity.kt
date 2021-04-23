package com.example.test.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * author: beitingsu
 * created on: 2021/4/23 10:28 AM
 */
class FragmentTestActivity: AppCompatActivity() {

    val mFragmentManagerWrapper by lazy {
        FragmentManagerWrapper(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            mFragmentManagerWrapper.showFragment(FragmentOne::class.java)
        }
    }

    override fun onBackPressed() {
        if (!mFragmentManagerWrapper.popBackFragment()) {
            super.onBackPressed()
        }
    }
}