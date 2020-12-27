package com.example.chuckapp.modules.auth.service

import com.example.chuckapp.model.User
import com.example.chuckapp.model.requestModels.auth.LoginRequest
import com.example.chuckapp.model.requestModels.auth.LoginRequestViaToken
import com.example.chuckapp.model.requestModels.auth.LoginResponse
import com.example.chuckapp.model.requestModels.auth.LoginResponseViaToken
import com.example.chuckapp.model.requestModels.signUp.EmailExitanceResponce
import com.example.chuckapp.model.requestModels.signUp.SignResponse
import com.example.chuckapp.util.Constants
import retrofit2.Call
import retrofit2.http.*

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

    @GET(Constants.EMAIL_EXISTANCE_CHECK_URL)
    fun checkEmailExistance(@Query("email") email : String) : Call<EmailExitanceResponce>





}