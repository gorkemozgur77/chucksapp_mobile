package com.example.chuckapp.model.friend

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReceivedRequest(
    @SerializedName("id")
    val id: String,

    @SerializedName("sender")
    val sender: Sender,

    @SerializedName("receiver")
    val receiever: Receiver,

    @SerializedName("status")
    val status: String,
): Serializable
