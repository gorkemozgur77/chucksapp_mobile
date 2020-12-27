package com.example.chuckapp.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object Constants {
    const val BASE_URL = "http://10.0.2.2:8081/"
    const val LOGIN_WITH_EMAIL_URL = "auth/sign-in"
    const val LOGIN_WITH_TOKEN_URL = "auth/refresh"
    const val SING_UP_URL = "auth/sign-up"
    const val EMAIL_EXISTANCE_CHECK_URL = "auth/email/exists"



    const val SIGN_OUT_URL ="auth/sign-out"
    const val GET_USER_INFO_URL = "me"





    const val PLATFORM_NAME = "MOBILE"
    const val TokenPreferenceFile = "TokenPreferenceFile"


}