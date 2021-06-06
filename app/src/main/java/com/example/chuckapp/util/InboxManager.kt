package com.example.chuckapp.util

import android.content.Context
import android.content.SharedPreferences
import com.example.chuckapp.model.friend.Friend
import com.example.chuckapp.model.friend.FriendRequestInbox
import com.example.chuckapp.model.friend.ReceivedRequest
import com.example.chuckapp.model.friend.SentRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class InboxManager(val context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences("InboxFile", Context.MODE_PRIVATE)

    companion object {
        const val FRIEND_INBOX = "friend_inbox"
        const val FRIEND_LIST = "friend_list"
    }

    fun saveInbox(inbox: FriendRequestInbox) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(inbox);
        editor.putString(FRIEND_INBOX, json);
        editor.apply()
    }

    fun saveFriendList(friend: MutableList<Friend>){
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(friend);
        editor.putString(FRIEND_LIST, json);
        editor.apply();
    }

    fun getInboxCount(): Int {
        val inboxManager = fetchReceivedRequests()
        if (inboxManager != null)
            return inboxManager.size
        return 0
    }

    fun fetchFriends(): MutableList<Friend>{
        val gson = Gson()
        val json = prefs.getString(FRIEND_LIST, null)
        return if (json != null) {
            val type: Type = object : TypeToken<List<Friend?>?>() {}.type
            gson.fromJson(json, type)
        } else mutableListOf()
    }

    private fun fetchInbox(): FriendRequestInbox? {
        val gson = Gson()
        val json = prefs.getString(FRIEND_INBOX, null)
        return gson.fromJson(json, FriendRequestInbox::class.java)
    }

    fun fetchSentRequests(): MutableList<SentRequest>? { return fetchInbox()?.sent
    }

    fun fetchReceivedRequests(): MutableList<ReceivedRequest>? { return fetchInbox()?.received
    }

    fun deleteAll() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
