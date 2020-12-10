package com.example.chuckapp.model.requestModels.signUp

import com.example.chuckapp.model.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MeResponse(
        @SerializedName("data")
        @Expose
        val user : User
)
