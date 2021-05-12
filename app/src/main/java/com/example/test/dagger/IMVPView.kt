package com.example.test.dagger

/**
 * author: beitingsu
 * created on: 2021/5/12 2:20 PM
 */
interface IMVPView {
    fun updateUI()
}

interface IMVPPresenter {
    fun loadData()
}