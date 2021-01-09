package com.zirouan.unphoto.data.remote.api

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.zirouan.unphoto.BuildConfig
import com.zirouan.unphoto.data.local.PreferencesHelper
import com.zirouan.unphoto.screen.detail.model.Detail
import com.zirouan.unphoto.screen.photo.model.Photo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface Api {

    class Factory {

        companion object {
            fun create(preferencesHelper: PreferencesHelper): Api {
                val okHttpClient: OkHttpClient.Builder =
                    OkHttpClient.Builder()
                        .connectTimeout(1, TimeUnit.MINUTES)
                        .readTimeout(1, TimeUnit.MINUTES)

                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY

                val requestInterceptor = RequestInterceptor(preferencesHelper)
                okHttpClient.addInterceptor(interceptor)
                    .addInterceptor(requestInterceptor)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .client(okHttpClient.build())
                    .build()

                return retrofit.create(Api::class.java)
            }
        }
    }

    @GET(ApiConstant.FETCH_PHOTOS)
    suspend fun fetchPhotos(
        @Query("page") page: Int,
        @Query("client_id") clientId: String,
        @Query("per_page") max: Int = 50
    ): List<Photo>

    @GET(ApiConstant.FETCH_TRACK_PHOTO)
    suspend fun fetchTrackPhoto(
        @Path("id") id: String,
        @Query("client_id") clientId: String
    ): Detail
}