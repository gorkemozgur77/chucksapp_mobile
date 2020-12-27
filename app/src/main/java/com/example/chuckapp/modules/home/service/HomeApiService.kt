package com.example.chuckapp.modules.home.service

import com.example.chuckapp.model.requestModels.auth.LoginResponse
import com.example.chuckapp.model.requestModels.signUp.MeResponse
import com.example.chuckapp.model.requestModels.signUp.SignOutRequest
import com.example.chuckapp.util.Constants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HomeApiService {

    @GET(Constants.GET_USER_INFO_URL)
    fun getProfile() : Call<MeResponse>

    @POST(Constants.SIGN_OUT_URL)
    fun signOut(@Body request: SignOutRequest) : Call<LoginResponse>

}