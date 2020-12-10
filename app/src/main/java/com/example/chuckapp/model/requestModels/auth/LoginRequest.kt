package com.example.chuckapp.model.requestModels.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginRequest(


        @SerializedName("email")
        @Expose
        val eposta : String,

        @SerializedName("password")
        @Expose
        val sifre : String,

        @SerializedName("platform")
        @Expose
        val loginPlatform : String,

        @SerializedName("ip")
        @Expose
        val ipAdress : String,

        @SerializedName("mac")
        @Expose
        val macAdresi : String,

        @SerializedName("deviceName")
        @Expose
        val device : String

)
