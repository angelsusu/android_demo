package com.example.test.dagger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.Common
import com.example.test.R
import com.example.test.debug
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_dagger_test.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

/**
 * author: beitingsu
 * created on: 2021/3/16 3:24 PM
 */
class DaggerActivity: AppCompatActivity() {
//
//    @Inject
//    lateinit var classA : ClassA
//
//
//    //@Named("param")
//    @ClassQualifier("param")
//    @Inject
//    lateinit var classB: ClassB
//
//    //@Named("noParam")
//    @ClassQualifier("noParam")
//    @Inject
//    lateinit var classBNoParam: ClassB
//
//    @Inject
//    lateinit var classC: ClassC
//
//    @Inject
//    lateinit var classCTwo: ClassC
//
//    @Inject
//    lateinit var classD: ClassD
//
//    @Inject
//    lateinit var classE: ClassE

    @Inject
    lateinit var classF: Lazy<ClassF>

    @Inject
    lateinit var classG: Provider<ClassG>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dagger_test)
        init()
    }

    private fun init() {
        //完成注入依赖关键
//        val appComponent = DaggerAppComponent.builder().build()
//
//        DaggerClassComponent.builder().value(2).appComponent(appComponent).build().inject(this)

        //父子组件
        val parentComponent = DaggerParentComponent.builder().build()
        parentComponent.subClassComponent().inject(this)

        btn_inject_test?.setOnClickListener {
//            debug(Common.TAG, "inject class:classA=${classA}" +
//                    ":classB=${classB}:${classB.classA}" +
//                    ":classBNoParam=${classBNoParam}:${classBNoParam.classA}}" +
//                    ":classC=${classC}:classCTwo=$classCTwo" +
//                    ":classD=${classD.value}" +
//                    ":classE=${classE}" )
            debug(Common.TAG, "classF=${classF.get()}:${classF.get()}:classG=${classG.get()}:${classG.get()}")
        }
    }
}