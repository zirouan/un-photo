package com.zirouan.unphoto.screen.login

import com.zirouan.unphoto.base.BaseValidatorHelper
import com.zirouan.unphoto.screen.login.validator.LoginValidatorHelper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {
    viewModel { LoginViewModel(get(), get(), get()) }
    factory<BaseValidatorHelper> { LoginValidatorHelper(get()) }
    factory<LoginContract.Repository> { LoginRepository(get()) }
}