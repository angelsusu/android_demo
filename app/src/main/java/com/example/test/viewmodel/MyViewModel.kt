package com.example.test.viewmodel

import androidx.lifecycle.ViewModel
import com.example.test.debug

/**
 * author: beitingsu
 * created on: 2021/1/9 10:35 AM
 */
class MyViewModel: ViewModel() {

    var mTestRestoreData = ""

    override fun onCleared() {
        super.onCleared()
        debug("onCleared")
    }
}