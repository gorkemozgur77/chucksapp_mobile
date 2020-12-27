package com.example.chuckapp.model.requestModels.signUp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EmailExitanceResponce(
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
        @SerializedName("existancy")
        @Expose
        val existance : String

)
