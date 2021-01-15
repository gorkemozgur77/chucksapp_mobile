package com.example.chuckapp.modules.home.view.appBarNavigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chuckapp.R
import kotlinx.android.synthetic.main.activity_add_friend_page.*


class AddFriendPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend_page)

        topAppBarFriend.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}