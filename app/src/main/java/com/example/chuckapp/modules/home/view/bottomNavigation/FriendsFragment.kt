package com.example.chuckapp.modules.home.view.bottomNavigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chuckapp.BaseFragment
import com.example.chuckapp.R
import com.example.chuckapp.modules.home.recyclerAdapters.FriendListRecyclerAdapter
import com.example.chuckapp.modules.home.view.HomePageActivity
import com.example.chuckapp.modules.home.view.appBarNavigation.AddFriendPage
import com.example.chuckapp.util.InboxManager
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.fragment_friends.*
import kotlinx.android.synthetic.main.fragment_friends.topAppBar

class FriendsFragment : BaseFragment() {

    lateinit var item : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        friendListRecyclerView.layoutManager = LinearLayoutManager(context)
        friendListRecyclerView.adapter = (activity as HomePageActivity).friendListRecyclerAdapter
        item = topAppBar.menu.findItem(R.id.addFriend).actionView


        item.setOnClickListener {
            navigateToAddFriend()
        }
        swipeRefreshLay.setOnRefreshListener {
            (activity as HomePageActivity).getInfo((activity as HomePageActivity).baseContext)
            swipeRefreshLay.isRefreshing = false
        }
    }


    private fun navigateToAddFriend() {

        startActivity(Intent(context, AddFriendPage::class.java))
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


}