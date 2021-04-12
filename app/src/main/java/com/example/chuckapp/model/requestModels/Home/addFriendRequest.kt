package com.example.chuckapp.model.requestModels.Home

import com.google.gson.annotations.SerializedName

data class addFriendRequest(

    @SerializedName("id")
    val id: String
) {
}