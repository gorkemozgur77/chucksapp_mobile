package com.example.chuckapp.model.friend

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FriendRequestInbox(

    @SerializedName("sent")
    val sent : List<SentRequest>,

    @SerializedName("received")
    val received : List<ReceivedRequest>

): Serializable