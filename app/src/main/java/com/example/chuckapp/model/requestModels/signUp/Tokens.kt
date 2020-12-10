package com.example.chuckapp.model.requestModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class tokens(
        @SerializedName("auth")
        @Expose
        var Token : String?,

        @SerializedName("perm_token")
        @Expose
        var PermToken :String?
)
