package com.example.test.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.test.R

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

        setContentView(R.layout.activity_fragment_test)

        if (savedInstanceState == null) {
            //mFragmentManagerWrapper.showFragment(FragmentOne::class.java)

            //参数传递测试
//            supportFragmentManager.commit {
//                replace(R.id.fragment_container, FragmentParamsTest("这是传递的参数"))
//            }

            //参数传递测试
            supportFragmentManager.commit {
                replace(R.id.fragment_container, FragmentParamsTest.newInstance("这是传递的参数"))
            }

        }
    }

    override fun onBackPressed() {
        if (!mFragmentManagerWrapper.popBackFragment()) {
            super.onBackPressed()
        }
    }
}