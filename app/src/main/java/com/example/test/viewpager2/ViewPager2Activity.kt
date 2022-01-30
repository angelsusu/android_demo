package com.example.test.viewpager2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import kotlinx.android.synthetic.main.activity_viewpager2.*

/**
 * author: beitingsu
 * created on: 2022/1/12 3:08 下午
 */
class ViewPager2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpager2)

        init()
    }

    private fun init() {
        btn_view?.setOnClickListener {
            ViewPager2TestActivity.startActivity(this, ViewPager2TestActivity.DATA_TYPE_VIEW)
        }
        btn_fragment?.setOnClickListener {
            ViewPager2TestActivity.startActivity(this, ViewPager2TestActivity.DATA_TYPE_FRAGMENT)
        }
        btn_tablayout?.setOnClickListener {
            ViewPager2TestActivity.startActivity(this, ViewPager2TestActivity.DATA_TYPE_TAB_LAYOUT, true)
        }
    }
}