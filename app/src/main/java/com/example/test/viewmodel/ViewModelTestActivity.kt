package com.example.test.viewmodel

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import com.example.test.debug
import kotlinx.android.synthetic.main.activity_viewmodel_test.*

/**
 * author: beitingsu
 * created on: 2021/1/9 10:34 AM
 */
class ViewModelTestActivity: AppCompatActivity() {

    //(1) 测试activity数据恢复
    private var mTestRestoreData = ""

    //(2) 测试使用viewModel数据恢复
    private val mViewModel by viewModels<MyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_viewmodel_test)

        init()
    }

    private fun init() {
        debug("init data:$mTestRestoreData")

        debug("init data:${mViewModel.mTestRestoreData}")

        btn_set_data?.setOnClickListener {
            mTestRestoreData = "test restore data"
            mViewModel.mTestRestoreData = "test restore data"  //省去自己保存数据的逻辑
            debug("set data:$mTestRestoreData")
        }
    }



    //activity 方式
    //屏幕旋转 保存数据
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("saveData", mTestRestoreData)
        super.onSaveInstanceState(outState)
    }

    //重建 恢复数据
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mTestRestoreData = savedInstanceState.getString("saveData", "")
        debug("restore data:$mTestRestoreData")
    }

    override fun getLastNonConfigurationInstance(): Any? {
        debug("beiting getLastNonConfigurationInstance")
        return super.getLastNonConfigurationInstance()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any? {
        debug("beiting onRetainCustomNonConfigurationInstance")
        return super.onRetainCustomNonConfigurationInstance()
    }
}