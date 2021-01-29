package com.example.test.coroutine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.debug
import kotlinx.coroutines.launch

/**
 * author: beitingsu
 * created on: 2021/1/9 10:35 AM
 */
class CoroutineViewModel: ViewModel() {

    fun test() {
        viewModelScope.launch {
            //todo
        }
    }

    override fun onCleared() {
        super.onCleared()
        debug("onCleared")
    }
}