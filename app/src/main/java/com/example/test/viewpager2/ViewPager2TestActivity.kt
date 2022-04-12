package com.example.test.viewpager2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.test.R
import com.example.test.commonDebug
import com.google.android.material.tabs.TabLayout
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


    private var holderArray = arrayOfNulls<ViewHolder>(2)

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
        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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
                viewpager.isUserInputEnabled = false
                viewpager.adapter = ViewPagerFragmentStateAdapter(this)
                TabLayoutMediator(tab_layout, viewpager) { tab, position ->
                    when (position) {
                        0 -> {
                            val hodler = getHolder()
                            tab.customView = hodler.view
                            hodler.textView.text = "Tab ${(position + 1)}"
                            hodler.textView.setTextColor(resources.getColor(R.color.colorAccent))
                            hodler.imageView.setImageResource(R.drawable.ic_rating_star_full)
                            holderArray[0] = hodler

                        }
                        1 -> {
                            val hodler2 = getHolder()
                            tab.customView = hodler2.view
                            hodler2.textView.text = "Tab ${(position + 1)}"
                            hodler2.textView.setTextColor(resources.getColor(R.color.colorPrimary))
                            hodler2.imageView.setImageResource(R.drawable.ic_rating_star_empty)
                            holderArray[1] = hodler2
                        }
                    }

                }.attach()
                //修改点击后的文字颜色 图标
                tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        when (tab?.position) {
                            0 -> {
                                holderArray[0]!!.textView.setTextColor(resources.getColor(R.color.colorPrimary))
                                holderArray[0]!!.imageView.setImageResource((R.drawable.ic_rating_star_empty))
                            }
                            1 -> {
                                holderArray[1]!!.textView.setTextColor(resources.getColor(R.color.colorAccent))
                                holderArray[1]!!.imageView.setImageResource((R.drawable.ic_rating_star_full))
                            }

                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        when (tab?.position) {
                            0 -> {
                                holderArray[0]!!.textView.setTextColor(resources.getColor(R.color.colorAccent))
                                holderArray[0]!!.imageView.setImageResource((R.drawable.ic_rating_star_full))
                            }
                            1 -> {
                                holderArray[1]!!.textView.setTextColor(resources.getColor(R.color.colorPrimary))
                                holderArray[1]!!.imageView.setImageResource((R.drawable.ic_rating_star_empty))
                            }

                        }
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {

                    }
                })
            }
        }
    }

    private fun getHolder() : ViewHolder {
        val v = LayoutInflater.from(applicationContext).inflate(R.layout.tab_view, null)
        val textView = v.findViewById(R.id.textview) as TextView
        val imageView = v.findViewById(R.id.imageview) as ImageView
        return ViewHolder(v, textView, imageView)
    }
}

data class ViewHolder(val view: View, val textView: TextView, val imageView: ImageView)