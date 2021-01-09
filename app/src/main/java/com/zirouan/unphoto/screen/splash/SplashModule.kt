package com.zirouan.unphoto.screen.splash

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {
    viewModel { SplashViewModel(get(), get()) }
    factory<SplashContract.Repository> { SplashRepository(get()) }
}