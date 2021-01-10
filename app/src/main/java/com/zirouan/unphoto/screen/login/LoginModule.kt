package com.zirouan.unphoto.screen.login

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {
    viewModel { LoginViewModel(get(), get()) }
    factory<LoginContract.Repository> { LoginRepository(get()) }
}