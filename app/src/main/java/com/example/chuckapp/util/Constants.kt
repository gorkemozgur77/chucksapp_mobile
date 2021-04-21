package com.example.chuckapp.util

import java.text.SimpleDateFormat
import java.util.*

object Constants {
    const val BASE_URL = "https://b2ebdc8d200d.ngrok.io/"
    const val LOCAL_BASE_URL = "http://10.0.2.2:8081/"
    const val LOGIN_WITH_EMAIL_URL = "auth/sign-in"
    const val LOGIN_WITH_TOKEN_URL = "auth/refresh"
    const val SING_UP_URL = "auth/sign-up"
    const val EMAIL_EXISTENCE_CHECK_URL = "auth/email/exists"

    const val SET_ONLINE_URL = "user-status/online"
    const val SET_OFFLINE_URL = "user-status/offline"


    const val SEND_TOKEN_URL = "user/fb/token"
    const val SEARCH_FRIEND_URL = "user/search"
    const val ADD_FRIEND_URL = "friend-request"


    const val ENTRY_ACTIVE_URL = "entry/active"
    const val ENTRY_INACTIVE_URL = "entry/inactive"
    const val SIGN_OUT_URL = "auth/sign-out"
    const val GET_USER_INFO_URL = "me"

    const val CALL_URL = "/call"
    const val PLATFORM_NAME = "MOBILE"
    const val TokenPreferenceFile = "TokenPreferenceFile"


    fun showError(throwable: Throwable) {
        println(throwable.cause)
        println(throwable.message)
        println(throwable.stackTrace)
    }

    fun getDate(time: Long): String {
        val cal = Calendar.getInstance()
        cal.timeZone
        val sdf = SimpleDateFormat("dd MMM yyyy, EEEE")
        sdf.timeZone = TimeZone.getTimeZone("GMT+3")
        val netDate = Date(time * 1000)
        return sdf.format(netDate)
    }


}