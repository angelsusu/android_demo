package com.example.test.dagger

import com.example.test.commonDebug
import javax.inject.Inject

/**
 * author: beitingsu
 * created on: 2021/5/12 2:22 PM
 */
class DaggerPresenter @Inject constructor(private val view: IMVPView) : IMVPPresenter {

    override fun loadData() {
        commonDebug("loadData")
    }
}