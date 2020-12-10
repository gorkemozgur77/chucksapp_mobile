package com.example.chuckapp.model.requestModels.signUp

import com.example.chuckapp.model.requestModels.tokens
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignResponse(
        @SerializedName("time")
        @Expose
        var zaman : String,

        @SerializedName("timestamp")
        @Expose
        var zamanstamp : Int,

        @SerializedName("status")
        @Expose
        var statu : String?,

        @SerializedName("message")
        @Expose
        var mesaj : String,

        @SerializedName("data")
        @Expose
        var tokenlar : tokens?,




        )
