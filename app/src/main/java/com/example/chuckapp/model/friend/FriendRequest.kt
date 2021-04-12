package com.example.chuckapp.model.friend

import com.google.gson.annotations.SerializedName

data class FriendRequest(
    @SerializedName("id")
    val id: String,

    @SerializedName("sender")
    val sender: Sender)
