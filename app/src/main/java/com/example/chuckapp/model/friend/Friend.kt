package com.example.chuckapp.model.friend

import android.view.View
import com.google.gson.annotations.SerializedName

data class Friend(



    @SerializedName("id")
    val id: String,

    @SerializedName("fullName")
    val fullname: String,

    var status: String



)
