package com.example.chuckapp.modules.home.service

import android.content.Context
import com.example.chuckapp.service.AuthInterceptor
import com.example.chuckapp.util.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeClient {

    private lateinit var homeApiService: HomeApiService

    fun getHomeApiService(context: Context): HomeApiService {

        if(!::homeApiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpClient(context))
                    .build()
            homeApiService = retrofit.create(HomeApiService::class.java)
        }
        return homeApiService
    }

    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .build()
    }

}