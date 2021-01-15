package com.example.chuckapp.modules.auth.service

import com.example.chuckapp.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthClient {

    private lateinit var authApiService: AuthApiService


    fun getAuthApiService(): AuthApiService {

        if(!::authApiService.isInitialized) {

            val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()


            authApiService = retrofit.create(AuthApiService::class.java)
        }

        return authApiService
    }

}