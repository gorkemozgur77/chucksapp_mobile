package com.example.chuckapp.model.requestModels.Home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InactiveResponse(
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

        @SerializedName("count")
        @Expose
        val count : Int,

        @SerializedName("nextUrl")
        @Expose
        val nexturl : String?,

        @SerializedName("data")
        @Expose
        val entry: List<Entry>?
)
