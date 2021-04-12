package com.example.chuckapp.modules.home.service

import com.example.chuckapp.model.FirebaseToken
import com.example.chuckapp.model.requestModels.Home.*
import com.example.chuckapp.model.requestModels.auth.LoginResponse
import com.example.chuckapp.model.requestModels.signUp.SignOutRequest
import com.example.chuckapp.util.Constants
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface HomeApiService {

    @GET(Constants.GET_USER_INFO_URL)
    fun getProfile(): Call<MeResponse>

    @POST(Constants.SIGN_OUT_URL)
    fun signOut(@Body request: SignOutRequest): Call<LoginResponse>

    @GET(Constants.ENTRY_ACTIVE_URL)
    fun getActiveEntries(): Call<ActiveResponse>

    @GET(Constants.ENTRY_INACTIVE_URL)
    fun getInactiveEntries(): Call<InactiveResponse>

    @GET
    fun getInactiveEntries(@Url page: String): Call<InactiveResponse>

    @POST(Constants.SET_ONLINE_URL)
    fun setOnline(): Call<ActiveInactiveResponse>

    @POST(Constants.SET_OFFLINE_URL)
    fun setOffline(): Call<ActiveInactiveResponse>

    @GET(Constants.SEARCH_FRIEND_URL)
    fun searchFriend(@Query("userName") userName: String): Call<FriendSearchRequestResponse>

    @POST(Constants.ADD_FRIEND_URL)
    fun addFriend(@Body requestId: addFriendRequest): Call<ResponseBody>

    @POST(Constants.ADD_FRIEND_URL+"/{user_id}/accept")
    fun acceptFriendRequest(@Path(value = "user_id", encoded = true) userId: String): Call<ResponseBody>

    @GET("friend-requests?role=RECEIVER")
    fun getFriendRequests(): Call<FriendInboxResponseModel>

    @POST(Constants.SEND_TOKEN_URL)
    fun sendFirebaseToken(@Body firebaseToken: FirebaseToken): Call<ResponseBody>

}