package com.example.chuckapp.modules.auth.service

import com.example.chuckapp.model.User
import com.example.chuckapp.model.requestModels.auth.LoginRequest
import com.example.chuckapp.model.requestModels.auth.LoginRequestViaToken
import com.example.chuckapp.model.requestModels.auth.LoginResponse
import com.example.chuckapp.model.requestModels.auth.LoginResponseViaToken
import com.example.chuckapp.model.requestModels.signUp.MeResponse
import com.example.chuckapp.model.requestModels.signUp.SignOutRequest
import com.example.chuckapp.model.requestModels.signUp.SignResponse
import com.example.chuckapp.util.Constants
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApiService {




    @Headers(
        "Content-Type: application/json"
    )
    @POST(Constants.SING_UP_URL)
    fun signUp(@Body request: User): Call<SignResponse>

    @POST(Constants.LOGIN_WITH_EMAIL_URL)
    fun signInViaEmail(@Body request: LoginRequest) : Call<LoginResponse>

    @POST(Constants.LOGIN_WITH_TOKEN_URL)
    fun signInViaToken(@Body request: LoginRequestViaToken) : Call<LoginResponseViaToken>

    @POST(Constants.SIGN_OUT_URL)
    fun signOut(@Body request: SignOutRequest) : Call<LoginResponse>

    @GET(Constants.GET_USER_INFO_URL)
    fun getProfile() : Call<MeResponse>



}