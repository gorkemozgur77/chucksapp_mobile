package com.example.chuckapp

import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    var isUserOnline = false

    enum class Type {

        HOME_PAGE, FRIEND_ADD, FRIEND_INBOX

    }

     fun onStopEto(type: Type) {
        println(type)
        onStop()


    }

    fun onStopFriend(){
        onStop()
    }

    override fun onStop() {
        super.onStop()
    }

}