package com.example.chuckapp.model.friend

import com.google.gson.annotations.SerializedName

data class Sender(
    @SerializedName("id")
    val id: String,

    @SerializedName("fullName")
    val fullname: String
)
