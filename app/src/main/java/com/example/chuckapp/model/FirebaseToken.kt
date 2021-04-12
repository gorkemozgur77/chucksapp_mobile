package com.example.chuckapp.model

import com.google.gson.annotations.SerializedName

data class FirebaseToken(

    @SerializedName("token")
    val firebaseToken: String
)
