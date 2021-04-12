package com.example.chuckapp.model

import com.example.chuckapp.model.Time
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Entry(

        @SerializedName("id")
        @Expose
        val id : String,

        @SerializedName("userId")
        @Expose
        val userid : String,

        @SerializedName("status")
        @Expose
        val statu : String,

        @SerializedName("platform")
        @Expose
        val plaformName : String,

        @SerializedName("ip")
        @Expose
        val ipAdress : String,

        @SerializedName("deviceName")
        @Expose
        val deviceInfo : String,

        @SerializedName("time")
        @Expose
        val time : Time




)
