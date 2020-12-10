package com.example.chuckapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("name")
    @Expose
    var isim: String,

    @SerializedName("lastName")
    @Expose
    var soyisim: String,

    @SerializedName("email")
    @Expose
    var eposta: String,

    @SerializedName("password")
    @Expose
    var sifre: String,

    @SerializedName("platform")
    @Expose
    var girisPlatform: String,

    @SerializedName("ip")
    @Expose
    var ipAdresi: String,

    @SerializedName("mac")
    @Expose
    var macAdresi: String,

    @SerializedName("deviceName")
    @Expose
    var cihazIsmi: String
)
