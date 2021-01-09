package com.zirouan.unphoto.screen.photo

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val photoModule = module {
    viewModel { PhotoViewModel(get(), get()) }
    factory<PhotoContract.Repository> { PhotoRepository(get()) }
}