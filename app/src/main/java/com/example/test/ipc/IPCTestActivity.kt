package com.example.test.ipc

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import com.example.test.*
import com.example.test.ipc.MessengerService.Companion.MESSAGE_FROM_CLIENT
import com.example.test.ipc.MessengerService.Companion.MESSAGE_FROM_SERVICE
import kotlinx.android.synthetic.main.activity_ipc_test.*

/**
 * author: beitingsu
 * created on: 2021/1/9 10:34 AM
 */
@SuppressLint("HandlerLeak")
class IPCTestActivity : AppCompatActivity() {

    //-------aidl测试 begin-------//
    private var mIsBind = false
    private var mServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            debug(Common.TAG, "onServiceDisconnected")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mIsBind = true
            mInterface = IServiceInterface.Stub.asInterface(service)
            debug(Common.TAG, "onServiceConnected:${mInterface?.bookInfo}")
            mInterface?.registListener(mCallback)
        }
    }

    private var mInterface: IServiceInterface? = null
    private val mCallback = object : IServiceCallback.Stub() {
        override fun callback() {
            debug(Common.TAG, "onServiceCallback")
        }
    }
    //-------aidl测试 end-------//


    //-------Messenger测试 begin  只能传递消息，不能跨进程调用方法-------//
    private var mIsBindMessenger = false
    private var mServiceMessenger: Messenger? = null

    private val mMessengerHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            //处理消息
            if (msg.what == MESSAGE_FROM_SERVICE) {
                debug(
                    Common.TAG, "receive message from service: ${msg.data.getString(
                        MessengerService.KEY_MESSAGE
                    )}"
                )
            }
        }
    }
    private val mClientMessenger = Messenger(mMessengerHandler)

    private var mMessengerServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            debug(Common.TAG, "onServiceDisconnected")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mIsBindMessenger = true
            mServiceMessenger = Messenger(service)

            //创建消息，通过Bundle传递数据
            val message = Message.obtain(null, MESSAGE_FROM_CLIENT)
            val bundle = Bundle()
            bundle.putString(MessengerService.KEY_MESSAGE, "hello service, this is client")
            message.data = bundle
            message.replyTo = mClientMessenger  //用于收取消息

            //向服务端发送消息
            mServiceMessenger?.send(message)
        }
    }
    //-------Messenger测试 end-------//


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ipc_test)

        init()
    }

    private fun init() {
        btn_aidl?.setOnClickListener {
            bindIPCService()
        }
        btn_messenger?.setOnClickListener {
            bindMessengerService()
        }
    }

    private fun bindIPCService() {
        bindService(
            Intent(this, IPCService::class.java),
            mServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun bindMessengerService() {
        bindService(
            Intent(this, MessengerService::class.java),
            mMessengerServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        mInterface?.unregistListener(mCallback)
        if (mIsBind) {
            unbindService(mServiceConnection)
        }
        if (mIsBindMessenger) {
            unbindService(mMessengerServiceConnection)
        }
    }

}