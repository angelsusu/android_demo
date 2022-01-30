package com.example.test.viewpager2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.test.R
import com.example.test.commonDebug
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_viewpager2_test.*

/**
 * author: beitingsu
 * created on: 2022/1/12 3:08 下午
 */
class ViewPager2TestActivity : AppCompatActivity() {

    companion object {
        private const val DATA_TYPE_KEY = "data_type"
        private const val NEED_SCROLL_KEY = "need_scroll"

        const val DATA_TYPE_VIEW = 1
        const val DATA_TYPE_FRAGMENT = 2
        const val DATA_TYPE_TAB_LAYOUT = 3

        fun startActivity(context: Context, type: Int, isNeedScroll: Boolean = true) {
            val intent = Intent(context, ViewPager2TestActivity::class.java).apply {
                putExtra(DATA_TYPE_KEY, type)
                putExtra(NEED_SCROLL_KEY, isNeedScroll)
            }
            context.startActivity(intent)
        }
    }

    private var mDataType = DATA_TYPE_VIEW
    private var mIsNeedScroll = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpager2_test)

        mDataType = intent.getIntExtra(DATA_TYPE_KEY, DATA_TYPE_VIEW)
        mIsNeedScroll = intent.getBooleanExtra(NEED_SCROLL_KEY, true)

        init()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun init() {
        viewpager.isUserInputEnabled = mIsNeedScroll
        viewpager.offscreenPageLimit = 1
        //viewpager.orientation = ViewPager2.ORIENTATION_VERTICAL
        viewpager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                commonDebug("onPageSelected:$position")
            }
        })
        when (mDataType) {
            DATA_TYPE_VIEW -> {
                val adapter = ViewPagerAdapter()
                adapter.setData(
                    mutableListOf(
                        "this is first page",
                        "this is second page", "this is third page"
                    )
                )
                viewpager.adapter = adapter
                adapter.notifyDataSetChanged()
            }
            DATA_TYPE_FRAGMENT -> {
                viewpager.adapter = ViewPagerFragmentStateAdapter(this)
            }
            DATA_TYPE_TAB_LAYOUT -> {
                viewpager.adapter = ViewPagerFragmentStateAdapter(this)
                TabLayoutMediator(tab_layout, viewpager) { tab, position ->
                    tab.text = "Tab ${(position + 1)}"
                }.attach()

            }
        }
    }
}