package com.zirouan.unphoto.screen.detail

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    viewModel { DetailViewModel(get(), get()) }
    factory<DetailContract.Repository> { DetailRepository(get()) }
}