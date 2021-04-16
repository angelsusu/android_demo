package com.example.test.dynamicproxy

import com.example.test.Common
import com.example.test.debug

/**
 * author: beitingsu
 * created on: 2021/4/16 3:59 PM
 */
class Sell : ISell {

    override fun sell() {
        debug(Common.TAG, "call sell method")
    }
}