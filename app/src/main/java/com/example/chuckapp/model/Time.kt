package com.example.chuckapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Time(

        @SerializedName("timestamp")
        @Expose
        val timestamp : Long



)
