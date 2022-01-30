package com.example.test.viewpager2

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.test.fragment.FragmentOne
import com.example.test.fragment.FragmentTwo

/**
 * author: beitingsu
 * created on: 2022/1/13 1:27 下午
 */
class ViewPagerFragmentStateAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

    companion object {
        const val COUNT = 2
        const val PAGE_ONE = 0
        const val PAGE_TWO = 1
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            PAGE_ONE -> {
                FragmentOne()
            }
            PAGE_TWO -> {
                FragmentTwo()
            }
            else -> FragmentOne()
        }
    }

    override fun getItemCount(): Int {
        return COUNT
    }
}