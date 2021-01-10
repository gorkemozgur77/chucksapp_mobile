package com.example.chuckapp.util

import retrofit2.Response

object Constants {
    const val BASE_URL = "http://34.65.51.174:8081/"
    const val LOCAL_BASE_URL = "http://10.0.2.2:8081/"
    const val LOGIN_WITH_EMAIL_URL = "auth/sign-in"
    const val LOGIN_WITH_TOKEN_URL = "auth/refresh"
    const val SING_UP_URL = "auth/sign-up"
    const val EMAIL_EXISTENCE_CHECK_URL = "auth/email/exists"



    const val SIGN_OUT_URL ="auth/sign-out"
    const val GET_USER_INFO_URL = "me"





    const val PLATFORM_NAME = "MOBILE"
    const val TokenPreferenceFile = "TokenPreferenceFile"





    fun showError(throwable: Throwable){
        println(throwable.cause)
        println(throwable.message)
        println(throwable.stackTrace)
    }


}