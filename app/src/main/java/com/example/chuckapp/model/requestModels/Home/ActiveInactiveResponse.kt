package com.example.chuckapp.model.requestModels.Home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ActiveInactiveResponse(
    @SerializedName("time")
    @Expose
    val zaman : String,

    @SerializedName("timestamp")
    @Expose
    val time_stamp : Int,

    @SerializedName("status")
    @Expose
    val statu : String,

    @SerializedName("message")
    @Expose
    val mesaj : String
)