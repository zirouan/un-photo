package com.zirouan.unphoto.base

import org.koin.core.module.Module

interface FragmentCompat {

    val module: Module?
    val viewModel: BaseViewModel?

    fun onInitView()
    fun onInitObserver()
    fun onFetchInitial()
    fun onError(message: String)
    fun onLoading(loading: Boolean)
}
