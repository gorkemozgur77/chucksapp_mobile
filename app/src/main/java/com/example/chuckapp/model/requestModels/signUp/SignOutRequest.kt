package com.example.chuckapp.model.requestModels.signUp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignOutRequest(
        @SerializedName("platform")
        @Expose
        val platformm : String
)
