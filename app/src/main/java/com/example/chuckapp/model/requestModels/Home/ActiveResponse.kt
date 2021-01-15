package com.example.chuckapp.model.requestModels.Home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ActiveResponse(
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
        val mesaj : String,

        @SerializedName("data")
        @Expose
        val entry : List<Entry>?
)
