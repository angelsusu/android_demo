package com.example.test

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.DONT_KILL_APP
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dianping.logan.Logan
import com.dianping.logan.LoganConfig
import com.example.test.activityresult.ResultActivity
import com.example.test.concurrent.BlockQueueTestActivity
import com.example.test.concurrent.ConcurrentTestActivity
import com.example.test.coroutine.CoroutineTestActivity
import com.example.test.dagger.DaggerActivity
import com.example.test.data.UserData
import com.example.test.data.UserDataWithDefault
import com.example.test.dynamicproxy.ISell
import com.example.test.dynamicproxy.Sell
import com.example.test.dynamicproxy.SellDynamicProxy
import com.example.test.expand.Child
import com.example.test.expand.Parent
import com.example.test.expand.expandParam
import com.example.test.expand.test
import com.example.test.flow.FlowTestActivity
import com.example.test.fragment.FragmentTestActivity
import com.example.test.gson.UserDataTypeAdapter
import com.example.test.ipc.IPCTestActivity
import com.example.test.lifecycle.LifecycleTestActivity
import com.example.test.log.LoganParser
import com.example.test.log.TestActivity
import com.example.test.multidex.HotFixActivity
import com.example.test.player.PlayerTestActivity
import com.example.test.room.RoomTestActivity
import com.example.test.rxjava.RxJavaActivity
import com.example.test.viewmodel.ViewModelTestActivity
import com.example.test.viewpager2.ViewPager2Activity
import com.example.test.widgets.WidgetsTestActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tencent.mars.xlog.Xlog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.*
import java.lang.reflect.Proxy
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue


open class MainActivity : AppCompatActivity() {

    private var mName = "Main"

    private val contract = object : ActivityResultContract<String, String>() {
        override fun createIntent(context: Context, input: String?): Intent {
            return Intent(context, ResultActivity::class.java).apply {
                putExtra(ResultActivity.INPUT_DATA_KEY, input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String {
            return intent?.getStringExtra(ResultActivity.OUTPUT_DATA_KEY) ?: ""
        }
    }
    private val launcher = registerForActivityResult(contract) { output ->
        commonDebug("registerForActivityResult:$output")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        btn_take_photo?.setOnClickListener {

            //checkPermissionAndCamera()

//            val i = Intent()
//            i.setClassName(
//                "com.sea.foody.nowmerchant.internal",
//                "com.sea.foody.nowmerchant.ui.splash.SplashActivity"
//            )
//            i.putExtra("serializable_key", UserData("test", 20))
//            startActivity(i)

            val formatter = BigDecimal(7.156f.toString())
            commonDebug("format:${formatter.setScale(1, RoundingMode.FLOOR)}")
//            commonDebug("format:${4.1f.toDouble()}:${formatter.format(4.1f.toDouble())}")
//            commonDebug("format:${4.2f.toDouble()}：${formatter.format(4.2f)}")
//            commonDebug("format:${4.3f.toDouble()}：${formatter.format(4.3f)}")
//            commonDebug("format:${formatter.format(4.4f)}")
//            commonDebug("format:${formatter.format(4.5f)}")

            rating_bar?.rating = 4.1f

            commonDebug("vm version:" + System.getProperty("java.vm.version"))
        }

        btn_take_video?.setOnClickListener {
            takeVideo()
        }

        btn_choose_pic?.setOnClickListener {
            checkPermissionAndChoosePic()
        }

        btn_choose_video?.setOnClickListener {
            checkPermissionAndChooseVideo()
        }
        btn_logger_logan?.setOnClickListener {
            loganTest()
        }
        btn_logger_parse?.setOnClickListener {
            loganParse()
        }
        btn_logger_xLog?.setOnClickListener {
            xLogTest()
        }
        btn_player_test?.setOnClickListener {
            startActivity(Intent(this, PlayerTestActivity::class.java))
        }
        btn_room_test?.setOnClickListener {
            startActivity(Intent(this, RoomTestActivity::class.java))
        }
        btn_lifecycle_test?.setOnClickListener {
            startActivity(Intent(this, LifecycleTestActivity::class.java))
        }
        btn_widgets_test?.setOnClickListener {
            startActivity(Intent(this, WidgetsTestActivity::class.java))
        }
        btn_viewModel_test?.setOnClickListener {
            startActivity(Intent(this, ViewModelTestActivity::class.java))
        }
        btn_coroutine_test?.setOnClickListener {
            startActivity(Intent(this, CoroutineTestActivity::class.java))
        }
        btn_dagger_test?.setOnClickListener {
            startActivity(Intent(this, DaggerActivity::class.java))
        }
        btn_rxjava_test?.setOnClickListener {
            startActivity(Intent(this, RxJavaActivity::class.java))
        }
        btn_dynamic_proxy_test?.setOnClickListener {
            //创建中介类实例
            val proxy = SellDynamicProxy(Sell())

            //获取代理类实例sell
            val sell = Proxy.newProxyInstance(
                Sell::class.java.classLoader,
                arrayOf<Class<*>>(
                    ISell::class.java
                ), proxy
            ) as? ISell

            //通过代理类对象调用代理类方法，实际上会转到invoke方法调用
            sell?.sell()
        }
        btn_gson_test?.setOnClickListener {
            fromGsonTest()
            toGsonTest()
        }
        btn_fragment_test?.setOnClickListener {
            startActivity(Intent(this, FragmentTestActivity::class.java))
        }
        btn_ipc_test?.setOnClickListener {
            startActivity(Intent(this, IPCTestActivity::class.java))
        }
        btn_concurrent_test?.setOnClickListener {
            startActivity(Intent(this, ConcurrentTestActivity::class.java))
        }

        btn_innerclass_test?.setOnClickListener {
            //匿名内部类
            innerClassTest()
        }
        btn_expand_test?.setOnClickListener {
            kotlinExpandTest()
        }
        btn_activity_result_test?.setOnClickListener {
            launcher.launch("registerForActivityResult test")
        }
        btn_block_queue_test?.setOnClickListener {
            startActivity(Intent(this, BlockQueueTestActivity::class.java))
        }
        btn_hot_fix_test?.setOnClickListener {
            startActivity(Intent(this, HotFixActivity::class.java))
        }
        btn_viewpager_test?.setOnClickListener {
            startActivity(Intent(this, ViewPager2Activity::class.java))
        }
        btn_change_icon_test?.setOnClickListener {
            val aliasComponent = ComponentName(
                this,
                "com.example.test.AliasActivity"
            )
            val mainComponent =  ComponentName(
                this,
                "com.example.test.MainActivity"
            )
            val aliasState: Int
            val mainState: Int
            if (packageManager.getComponentEnabledSetting(aliasComponent) ==
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            ) {
                aliasState = PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                mainState = PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            } else {
                aliasState = PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                mainState = PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            }
            packageManager.setComponentEnabledSetting(
                aliasComponent, aliasState,
                DONT_KILL_APP
            )
            packageManager.setComponentEnabledSetting(
                mainComponent, mainState, DONT_KILL_APP
            )
        }
        btn_flow_test?.setOnClickListener {
            startActivity(Intent(this, FlowTestActivity::class.java))
        }
    }

    private fun openCamera() {
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(captureIntent, CAMERA_REQUEST_CODE)
    }

    private fun takeVideo() {
        val intent = Intent()
        intent.action = "android.media.action.VIDEO_CAPTURE"
        startActivityForResult(intent, 0)
    }

    private fun choosePhoto() {
        val intentToPickPic = Intent(Intent.ACTION_PICK, null)
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intentToPickPic, GALLERY_REQUEST_CODE)

    }

    private fun chooseVideo() {
        val intentToPickPic = Intent(Intent.ACTION_PICK, null)
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "video/*")
        startActivityForResult(intentToPickPic, GALLERY_REQUEST_CODE)
    }

    private fun checkPermissionAndCamera() {
        val hasCameraPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            //有调起相机拍照。
            openCamera()
        } else {
            val array = arrayOf(Manifest.permission.CAMERA)
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(
                this, array,
                PERMISSION_CAMERA_REQUEST_CODE
            )
        }
    }

    private fun checkPermissionAndChoosePic() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            //有权限
            choosePhoto()
        } else {
            //权限还没有授予，需要在这里写申请权限的代码
            // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
            // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), GALLERY_REQUEST_CODE
            )

        }
    }

    private fun checkPermissionAndChooseVideo() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            //有权限
            chooseVideo()
        } else {
            //权限还没有授予，需要在这里写申请权限的代码
            // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
            // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), GALLERY_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CAMERA_REQUEST_CODE -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    //允许权限，有调起相机拍照
                    openCamera()
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_LONG).show()
                }
            }
            GALLERY_REQUEST_CODE -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    //允许权限，有调起相机拍照
                    choosePhoto()
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this, "查看相册权限被拒绝", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //美团logan测试
    private fun loganTest() {
        val dir = applicationContext.filesDir.absolutePath + "logan_v1"
        val file = File(dir)
        if (!file.exists()) {
            file.mkdirs()
        }
        //初始化
        val config = LoganConfig.Builder()
            .setCachePath(dir)
            .setPath(
                (applicationContext.getExternalFilesDir(null)?.absolutePath
                        + File.separator) + "logan" + File.separator + "logan_v1"
            )
            .setEncryptKey16("0123456789012345".toByteArray())
            .setEncryptIV16("0123456789012345".toByteArray())
            .build()
        Logan.init(config)
        Logan.setDebug(true)
        Logan.setOnLoganProtocolStatus { cmd, code ->
            Log.d("loganTest", "$cmd:$code")
        }

        //test
        loganWrite()
    }

    //美团logan解密测试
    private fun loganParse() {
        val parser = LoganParser(
            "0123456789012345".toByteArray(),
            "0123456789012345".toByteArray()
        )
        val map = Logan.getAllFilesInfo()
        for ((key, value) in map) {
            val name = getDateToString(key)
            val fileName =
                applicationContext.getExternalFilesDir(null)?.absolutePath + File.separator + "logan_v1/" + name
            val outFileName =
                applicationContext.getExternalFilesDir(null)?.absolutePath + File.separator + "logan_v1/" + key + "_parse.txt"
            Log.d("loganTest", "$fileName")
            var inputStream: InputStream? = null
            var i = 0
            try {
                inputStream = FileInputStream(File(fileName))
                val out = FileOutputStream(File(outFileName))
                parser.parse(inputStream, out)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getDateToString(str: String): Long {
        var dateFormat = SimpleDateFormat("yyyy-MM-dd");
        var date = Date();
        try {
            date = dateFormat.parse(str);
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        return date.getTime()
    }

    private fun loganWrite() {
        Thread(Runnable {
            var i = 0
            while (i < 1000) {
                Logan.w("subeiting test logan:$i", 2)
                Log.d("loganTest", "$i")
                ++i
                Thread.sleep(5)
            }
            Log.d("loganTest", "finish loganTest")
            Logan.f()
            loganParse()
        }).start()
    }

    //微信 xLog测试
    private fun xLogTest() {
        //初始化
        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");

        val SDCARD = getExternalFilesDir("/xlog")?.absolutePath
        val logPath = SDCARD

        // this is necessary, or may crash for SIGBUS
        val cachePath = this.getFilesDir().absolutePath + "/xlog"

        Log.d("xLogTest", "$logPath")
        Xlog.appenderOpen(
            Xlog.LEVEL_DEBUG, Xlog.AppednerModeAsync, cachePath, logPath,
            "MarsSample", 0, ""
        );
        Xlog.setConsoleLogOpen(true);

        com.tencent.mars.xlog.Log.setLogImp(Xlog())

        //test
        com.tencent.mars.xlog.Log.d("xLogTest", "", "", 0, 0, 0, 0L, "test xlog")
        startActivity(Intent(this, TestActivity::class.java))

    }

    //todo 测试数据结构
    private fun testMap() {
        val map = ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>()

        GlobalScope.launch {
            map.getOrPut("test") {
                ConcurrentLinkedQueue()
            }?.add("test1")
        }

        GlobalScope.launch {
            map.getOrPut("test") {
                ConcurrentLinkedQueue()
            }?.add("test2")
        }

        GlobalScope.launch {
            map.getOrPut("test") {
                ConcurrentLinkedQueue()
            }?.add("test3")
        }
        GlobalScope.launch {
            delay(3000)
            map["test"]?.forEach { str ->
                Log.d("subeiting", "test map:$str")
            }
        }
    }


    private fun fromGsonTest() {
        //使用自定义的解析器
        //val gson = GsonBuilder().registerTypeAdapter(UserData::class.java, UserDataTypeAdapter()).create()
        //val data1 = gson.fromJson<UserData>("{\"name\":\"subeiting\"}", UserData::class.java)

        val data1 = Gson().fromJson<UserData>(
            "{\"name\":\"subeiting\", \"age\":\"\"}",
            UserData::class.java
        )

        val data2 = Gson().fromJson<UserDataWithDefault>(
            "{\"name\":null}",
            UserDataWithDefault::class.java
        )
        debug(Common.TAG, "gson test: $data1: $data2")
    }


    private fun toGsonTest() {
        //使用自定义的解析器
        val gson =
            GsonBuilder().registerTypeAdapter(UserData::class.java, UserDataTypeAdapter()).create()
        val str = gson.toJson(UserData("beiting", 12))

        debug(Common.TAG, "gson test: $str")
    }


    private fun innerClassTest() {

        InnerClass().print()

        //方法内部类，是不能有public、protected、private 以及 static 修饰符的，作用域仅限该方法内
        class PartialClass {
            fun print() {
                commonDebug("this is PartialClass")
            }
        }

        PartialClass().print()

        StaticClass().print()

    }

    //成员内部类，可以引用外部类的变量，外部实例化需要先持有外部类的实例
    inner class InnerClass {
        fun print() {
            commonDebug("this is innerClass:$mName")
        }
    }


    //静态内部类，不能引用外部类，可以直接实例化
    class StaticClass {
        fun print() {
            commonDebug("this is StaticClass")
        }
    }

    private fun kotlinExpandTest() {
        val parent = Parent()
        val child1: Parent = Child()
        val child2 = Child()
        parent.print()
        parent.test()
        child1.test() //调用的是父类的方法
        child2.test()
        parent.expandParam = "hello helo"
        commonDebug("parent expandParam:${parent.expandParam}")
    }

    companion object {
        private const val PERMISSION_CAMERA_REQUEST_CODE = 100
        private const val CAMERA_REQUEST_CODE = 101
        private const val GALLERY_REQUEST_CODE = 102

    }
}
