package com.example.test.shortcut

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.test.MainActivity
import com.example.test.R
import kotlinx.android.synthetic.main.activity_shortcut_test.*

/**
 * author: beitingsu
 * created on: 2022/4/12 3:01 下午
 */
class ShortcutTestActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shortcut_test)

        init()
    }

    private fun init() {
        btn_create?.setOnClickListener {
            createShortcut()
        }
    }

    private fun createShortcut() {
        // 创建 Shortcut
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (isShortcutCreated()) {
                    Log.d("MyTest", "true pinned")
                } else {
                    val targetIntent = Intent(this, MainActivity::class.java).apply {
                        action = "android.intent.action.Pinned"
                    }

                    Glide.with(this).asBitmap().load("http://www.08931.com/uploads/allimg/202105/xjweiifzv1c.jpg").into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap?,
                            transition: Transition<in Bitmap>?
                        ) {
                            val pinShortcutInfo = ShortcutInfoCompat.Builder(this@ShortcutTestActivity, "pinned")
                                .setShortLabel("测试固定快捷方式")
                                .setLongLabel("测试固定快捷方式")
                                .setIcon(IconCompat.createWithAdaptiveBitmap(resource))
                                .setIntent(targetIntent)
                                .build()

                            ShortcutManagerCompat.requestPinShortcut(
                                this@ShortcutTestActivity, pinShortcutInfo,
                                ShortcutReceiver.getPinRequestAcceptedIntent(this@ShortcutTestActivity)?.intentSender
                            )
                        }
                    })
                }
            } else {
                // 旧实现
                val addShortcutIntent = Intent("com.android.launcher.action.INSTALL_SHORTCUT")
                // 不允许重复创建，不是根据快捷方式的名字判断重复的
                addShortcutIntent.putExtra("duplicate", false)
                addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "测试固定快捷方式")
                //图标
                addShortcutIntent.putExtra(
                    Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher)
                )
                // 设置关联程序
                val launcherIntent = Intent()
                launcherIntent.setClass(this, MainActivity::class.java)
                addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent)
                // 发送广播
                sendBroadcast(addShortcutIntent)
            }
        } else {
            Toast.makeText(this, "设备不支持Pinned Shortcut", Toast.LENGTH_SHORT).show()
        }
    }

    // 自定义 BroadcastReceiver
    class ShortcutReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "创建成功", Toast.LENGTH_SHORT).show()
        }

        companion object{
            fun getPinRequestAcceptedIntent(context: Context?): PendingIntent? {
                val intent = Intent(Intent.ACTION_CREATE_SHORTCUT).apply {
                    context?.let {
                        component = ComponentName(it, ShortcutReceiver::class.java)
                    }
                }
                return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }

    // 根据 id 判断是否重复创建
    private fun isShortcutCreated() : Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val shortcutManager = getSystemService(ShortcutManager::class.java)
            val shortcutInfos = shortcutManager.pinnedShortcuts
            for(i in shortcutInfos){
                if(TextUtils.equals(i.id,"id")){
                    return true
                }else{
                    continue
                }
            }
        }
        return false
    }

}
