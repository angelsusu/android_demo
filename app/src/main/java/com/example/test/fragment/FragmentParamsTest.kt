package com.example.test.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test.Common
import com.example.test.R
import com.example.test.debug
import kotlinx.android.synthetic.main.frgament_test.*

/**
 * author: beitingsu
 * created on: 2021/4/23 10:30 AM
 */
class FragmentParamsTest(private val text: String = "default"): Fragment() {


    companion object {
        fun newInstance(text: String): FragmentParamsTest {
            val fragment = FragmentParamsTest("")
            val bundle = Bundle()
            bundle.putString("text", text)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.frgament_test, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        debug(Common.TAG, "FragmentParamsTest Param:$text")
//
//        btn_text?.text = text

        val param = arguments?.getString("text")

        debug(Common.TAG, "FragmentParamsTest Param:$param")

        btn_text?.text = param

        btn_jump?.visibility = View.GONE
    }

}