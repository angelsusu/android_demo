package com.example.test.ipc

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.*
import com.example.test.Common
import com.example.test.debug

/**
 * author: beitingsu
 * created on: 2021/4/25 4:16 PM
 */
@SuppressLint("HandlerLeak")
class MessengerService: Service() {

    companion object {
        const val MESSAGE_FROM_CLIENT= 1
        const val MESSAGE_FROM_SERVICE = 2
        const val KEY_MESSAGE = "msg"
    }

    private val mMessengerHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            //处理消息
            if (msg.what == MESSAGE_FROM_CLIENT) {
                debug(Common.TAG, "receive message from client: ${msg.data.getString(KEY_MESSAGE)}")

                //发送消息给client
                val message = Message.obtain(null, MESSAGE_FROM_SERVICE)
                val bundle = Bundle()
                bundle.putString(KEY_MESSAGE, "hello client, this is service")
                message.data = bundle

                //获取客户端传递过来的Messenger，通过这个Messenger回传消息给客户端
                msg.replyTo?.send(message)
            }
        }
    }


    private val mMessenger = Messenger(mMessengerHandler)


    override fun onBind(intent: Intent?): IBinder? {
        return mMessenger.binder
    }
}