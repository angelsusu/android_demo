package com.example.test.multidex

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.test.R
import kotlinx.android.synthetic.main.activity_hot_fix_test.*

/**
 * author: beitingsu
 * created on: 2021/11/25 2:51 下午
 */
class HotFixActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hot_fix_test)
        initView()

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            val array = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            //没有权限，申请权限，先申请权限 后push dex文件，单纯方便测试
            //正常逻辑可以将下载的文件放在应用内部目录，就不需要申请权限了
            ActivityCompat.requestPermissions(
                this, array,
                1
            )
        }
    }

    private fun initView() {
        btn_click?.setOnClickListener {
            tv_hot_fix?.text = ToBeFixed.get()
        }
    }
}