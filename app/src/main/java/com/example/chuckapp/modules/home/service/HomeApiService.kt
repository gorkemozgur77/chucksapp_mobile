package com.example.chuckapp.modules.home.service

import com.example.chuckapp.model.requestModels.Home.ActiveResponse
import com.example.chuckapp.model.requestModels.Home.InactiveResponse
import com.example.chuckapp.model.requestModels.auth.LoginResponse
import com.example.chuckapp.model.requestModels.Home.MeResponse
import com.example.chuckapp.model.requestModels.signUp.SignOutRequest
import com.example.chuckapp.util.Constants
import retrofit2.Call
import retrofit2.http.*

interface HomeApiService {

    @GET(Constants.GET_USER_INFO_URL)
    fun getProfile() : Call<MeResponse>

    @POST(Constants.SIGN_OUT_URL)
    fun signOut(@Body request: SignOutRequest) : Call<LoginResponse>

    @GET(Constants.ENTRY_ACTIVE_URL)
    fun getActiveEntries() : Call<ActiveResponse>

    @GET(Constants.ENTRY_INACTIVE_URL)
    fun getInactiveEntries() : Call<InactiveResponse>

    @GET
    fun getInactiveEntries(@Url page : String) : Call<InactiveResponse>

}