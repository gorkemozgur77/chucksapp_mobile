package com.example.chuckapp.modules.home.view.bottomNavigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chuckapp.BaseFragment
import com.example.chuckapp.R
import com.example.chuckapp.modules.home.view.HomePageActivity
import com.example.chuckapp.modules.home.view.appBarNavigation.AddFriendPage
import com.example.chuckapp.modules.home.view.appBarNavigation.FriendInboxActivity
import kotlinx.android.synthetic.main.fragment_bottomfragment1.*

class Bottomfragment1 : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_bottomfragment1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendListRecyclerView.layoutManager = LinearLayoutManager(context)
        friendListRecyclerView.adapter = (activity as HomePageActivity).friendListRecyclerAdapter

        topAppBar.setOnMenuItemClickListener {
            (activity as HomePageActivity).isUserOnline = true
            when (it.itemId) {
                R.id.addFriend -> navigateToAddFriend()
                R.id.friendRequestBox -> {
                    startActivity(Intent(context, FriendInboxActivity::class.java))
                    activity?.overridePendingTransition(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                    )
                }
            }
            true
        }
    }



    private fun navigateToAddFriend() {

        startActivity(Intent(context, AddFriendPage::class.java))
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }



}