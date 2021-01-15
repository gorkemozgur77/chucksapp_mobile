package com.example.chuckapp.modules.home.view.bottomNavigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chuckapp.R
import com.example.chuckapp.modules.home.view.appBarNavigation.AddFriendPage
import kotlinx.android.synthetic.main.fragment_bottomfragment1.*

class Bottomfragment1 : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottomfragment1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.addFriend -> NavigateToAddfriend()
            }
            true
        }
    }

    fun NavigateToAddfriend(){
        startActivity(Intent(context, AddFriendPage::class.java))
        activity?.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
    }

}