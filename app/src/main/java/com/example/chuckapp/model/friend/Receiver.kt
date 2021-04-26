package com.example.chuckapp.model.friend

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Receiver(
    @SerializedName("id")
    val id: String,

    @SerializedName("fullName")
    val fullname: String
): Serializable
