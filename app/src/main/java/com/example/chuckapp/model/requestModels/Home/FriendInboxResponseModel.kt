package com.example.chuckapp.model.requestModels.Home

import com.example.chuckapp.model.friend.FriendRequest
import com.google.gson.annotations.SerializedName

data class FriendInboxResponseModel(
    @SerializedName("time")
    val time: String,

    @SerializedName("timestamp")
    val timestamp: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("count")
    val count: Int,

    @SerializedName("data")
    val data: List<FriendRequest>
)