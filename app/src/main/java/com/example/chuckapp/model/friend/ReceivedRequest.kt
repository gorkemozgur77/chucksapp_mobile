package com.example.chuckapp.model.friend

import com.example.chuckapp.model.Time
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReceivedRequest(
    @SerializedName("id")
    val id: String,

    @SerializedName("sender")
    val sender: Sender,

    @SerializedName("receiver")
    val receiver: Receiver,

    @SerializedName("status")
    val status: String,

    @SerializedName("created")
    val time: Time
): Serializable
