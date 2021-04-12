package com.example.chuckapp.modules.home.view.bottomNavigation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chuckapp.BaseFragment
import com.example.chuckapp.R
import com.example.chuckapp.model.requestModels.Home.MeResponse
import com.example.chuckapp.modules.home.recyclerAdapters.FriendListRecyclerAdapter
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.modules.home.view.HomePageActivity
import com.example.chuckapp.modules.home.view.appBarNavigation.AddFriendPage
import com.example.chuckapp.modules.home.view.appBarNavigation.FriendInboxActivity
import com.example.chuckapp.util.Constants
import kotlinx.android.synthetic.main.fragment_bottomfragment1.*
import retrofit2.Call
import retrofit2.Response

class Bottomfragment1 : BaseFragment() {

    lateinit var friendListRecyclerAdapter: FriendListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottomfragment1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendListRecyclerAdapter = context?.let { FriendListRecyclerAdapter(it) }!!

        friendListRecyclerView.layoutManager = LinearLayoutManager(context)
        friendListRecyclerView.adapter = friendListRecyclerAdapter

        getInfo(requireContext())

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

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            println("From broadcast    ------     " + intent.extras?.get("userId"))
            friendListRecyclerAdapter.changeStatus(
                intent.extras?.get("userId").toString(),
                intent.extras?.get("action").toString()
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            (mMessageReceiver),
            IntentFilter("MyData")
        )
    }

    private fun navigateToAddFriend() {

        startActivity(Intent(context, AddFriendPage::class.java))
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun getInfo(context: Context) {
        showProgressBar()
        HomeClient().getHomeApiService(context).getProfile()
            .enqueue(object : retrofit2.Callback<MeResponse> {

                override fun onResponse(call: Call<MeResponse>, response: Response<MeResponse>) {
                    hideProgressBar()
                    println(response.body())
                    if (response.isSuccessful) {
                        response.body()?.user?.let { friendListRecyclerAdapter.updateFriendList(it.friends) }
                    }
                }

                override fun onFailure(call: Call<MeResponse>, t: Throwable) {
                    Constants.showError(t)
                }
            })
    }

}