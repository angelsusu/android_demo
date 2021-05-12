package com.example.test.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test.MainActivity
import com.example.test.R
import kotlinx.android.synthetic.main.frgament_test.*

/**
 * author: beitingsu
 * created on: 2021/4/23 10:30 AM
 */
class FragmentOne: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.frgament_test, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_text?.text = "这是FragmentOne"

        btn_jump?.setOnClickListener {
            (activity as? FragmentTestActivity)?.mFragmentManagerWrapper?.showFragment(
                FragmentTwo::class.java, canPopBack = true)
        }
    }
}