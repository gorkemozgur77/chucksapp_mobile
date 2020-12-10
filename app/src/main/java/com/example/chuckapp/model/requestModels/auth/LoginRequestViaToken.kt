package com.example.chuckapp.model.requestModels.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginRequestViaToken(

        @SerializedName("token")
        @Expose
        val permToken : String?,

        @SerializedName("platform")
        @Expose
        val girisPlatformu : String?

)
