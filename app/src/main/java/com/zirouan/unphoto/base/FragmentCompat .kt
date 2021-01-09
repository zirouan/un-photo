package com.zirouan.unphoto.base

import org.koin.core.module.Module

interface FragmentCompat {

    val module: Module?
    val viewModel: BaseViewModel?

    fun initView()
    fun initObservers()
    fun fetchInitialData()
    fun onError(message: String)
    fun onLoading(isLoading: Boolean)
}
