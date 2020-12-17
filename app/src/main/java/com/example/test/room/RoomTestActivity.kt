package com.example.test.room

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import kotlinx.android.synthetic.main.activity_room_test.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * author: beitingsu
 * created on: 2020/5/13 2:06 PM
 */
class RoomTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_test)

        init()
    }

    private fun init() {
        val userInfo = UserInfo(
            uid = UUID.randomUUID().toString(),
            name = "beiting.su",
            age = 20,
            isMale = 2
        )
        var userDB: UserInfoDB? = null
        btn_room_create?.setOnClickListener {
            userDB = UserInfoDB.build(this, userInfo.uid)
        }
        btn_room_write?.setOnClickListener {
            GlobalScope.launch {
                userDB?.userInfoDao()?.insert(userInfo)
            }
        }
        btn_room_read?.setOnClickListener {
            GlobalScope.launch {
                val list = userDB?.userInfoDao()?.getByIds(userInfo.uid)
                list?.forEach { userInfo ->
                    Log.d("RoomTestActivity", "$userInfo")
                }
            }
        }
        btn_room_delete?.setOnClickListener {
            GlobalScope.launch {
                userDB?.userInfoDao()?.delete(userInfo)
            }
        }
    }

}