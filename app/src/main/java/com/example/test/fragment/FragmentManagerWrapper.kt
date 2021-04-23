package com.example.test.fragment

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.test.R

/**
 * @author vianhuang
 * @date 2020/7/14 11:32 AM
 */

private const val TAG = "FragmentManagerWrapper"

class FragmentManagerWrapper(private val activity: AppCompatActivity) {
    private val mFragmentManager = activity.supportFragmentManager

    /**
     * 显示指定Fragment
     * @param containerViewId fragment的容器viewId
     * @param fragmentClass 需要显示的fragment.class
     * @param args 初始化fragment时带的参数
     * @param canPopBack 是否添加到回退栈中
     */
    fun showFragment(
        fragmentClass: Class<out Fragment>,
        containerViewId: Int = R.id.fragment_container_view,
        args: Bundle? = null,
        canPopBack: Boolean = false
    ) {
        try {
            //1. 如果有fragment实例直接复用，否则创建新的fragment实例
            val fragment: Fragment = getOrCreateFragment(fragmentClass, args)

            //2. 检测fragment容器是否添加
            checkContainer(containerViewId)

            mFragmentManager.commit(allowStateLoss = true) {

                val tag = getFragmentTag(fragmentClass)

                //3. 将fragment实例添加进FragmentManager
                if (!fragment.isAdded) {
                    add(containerViewId, fragment, tag)
                }

                //4. 隐藏当前可见的fragment，显示新添加的fragment
                getCurrentFragment()?.let { hide(it) }
                show(fragment)

                //5. 添加进回退栈
                if (canPopBack) {
                    addToBackStack(tag)
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "show fragment error. exception = ${e.message}")

        }
    }

    /**
     * 回退Fragment
     */
    fun popBackFragment(): Boolean {
        if (mFragmentManager.backStackEntryCount > 0) {
            try {
                return mFragmentManager.popBackStackImmediate()
            } catch (e: Exception) {
                Log.e(TAG, "pop back fragment error. exception = ${e.message}")
            }
        }

        return false
    }

    /**
     * 获取当前可见的fragment
     */
    fun getCurrentFragment(): Fragment? {
        return mFragmentManager.fragments.lastOrNull { it.isVisible }
    }

    /**
     * 获取fragment的标签
     */
    fun getFragmentTag(fragmentClass: Class<out Fragment>) = fragmentClass.simpleName

    /**
     * 获取或者创建指定fragment
     */
    fun getOrCreateFragment(
        fragmentClass: Class<out Fragment>,
        args: Bundle? = null
    ): Fragment {
        return getFragment(fragmentClass) ?: createFragment(fragmentClass, args)
    }

    /**
     * 获取已添加的指定fragment
     */
    fun getFragment(fragmentClass: Class<out Fragment>): Fragment? {
        val tag = getFragmentTag(fragmentClass)
        return mFragmentManager.findFragmentByTag(tag)
    }

    /**
     * 创建指定fragment
     */
    fun createFragment(
        fragmentClass: Class<out Fragment>,
        args: Bundle? = null
    ): Fragment {
        return fragmentClass.newInstance().apply {
            arguments = args
        }
    }

    private fun checkContainer(containerViewId: Int) {
        if (containerViewId > 0) {
            var container = activity.findViewById<FrameLayout>(containerViewId)
            if (container == null) {
                container = FrameLayout(activity).apply {
                    id = R.id.fragment_container_view
                }
                activity.addContentView(
                    container,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
            }
        }
    }
}