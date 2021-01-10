package com.zirouan.unphoto.di

import com.zirouan.unphoto.data.local.PreferencesHelper
import com.zirouan.unphoto.data.local.PreferencesHelperImpl
import com.zirouan.unphoto.data.remote.api.Api
import com.zirouan.unphoto.data.remote.apihelper.ApiHelper
import com.zirouan.unphoto.data.remote.apihelper.ApiHelperImpl
import com.zirouan.unphoto.util.exception.ExceptionHelper
import com.zirouan.unphoto.util.exception.ExceptionHelperImpl
import org.koin.dsl.module

val appModule = module {
    single { Api.Factory.create(get()) }
    single<ApiHelper> { ApiHelperImpl(get()) }
    single<PreferencesHelper> { PreferencesHelperImpl(get()) }
    single<ExceptionHelper> { ExceptionHelperImpl(get()) }
}