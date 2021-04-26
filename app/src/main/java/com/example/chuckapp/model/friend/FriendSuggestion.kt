package com.example.chuckapp.model.friend

import android.os.Parcel
import android.os.Parcelable
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.gson.annotations.SerializedName

data class FriendSuggestion(

    @SerializedName("id")
    val id: String?,

    @SerializedName("fullName")
    val fullName: String?,

    var mIsHistory: Boolean = false

) : SearchSuggestion {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(fullName)
    }

    override fun getBody(): String? {
        return fullName
    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FriendSuggestion> {
        override fun createFromParcel(parcel: Parcel): FriendSuggestion {
            return FriendSuggestion(parcel)
        }

        override fun newArray(size: Int): Array<FriendSuggestion?> {
            return arrayOfNulls(size)
        }
    }

}

