package com.example.chuckapp.modules.home.view.appBarNavigation

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chuckapp.BaseActivity
import com.example.chuckapp.R
import com.example.chuckapp.model.requestModels.Home.FriendInboxResponseModel
import com.example.chuckapp.modules.home.recyclerAdapters.FriendInboxRecyclerAdapter
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.util.Constants
import kotlinx.android.synthetic.main.activity_friend_inbox.*
import retrofit2.Call
import retrofit2.Response

class FriendInboxActivity : BaseActivity() {
    private lateinit var friendInboxRecyclerAdapter: FriendInboxRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_inbox)
        friendInboxRecyclerAdapter = FriendInboxRecyclerAdapter(this)

        friendRequestInboxRecyclerView.apply {
            adapter = friendInboxRecyclerAdapter
            layoutManager = LinearLayoutManager(applicationContext)
        }

        topAppBarFriend.setNavigationOnClickListener {
            onBackPressed()
        }

        getFriendRequest()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun getFriendRequest() {
        HomeClient().getHomeApiService(this).getFriendRequests().enqueue(object :
            retrofit2.Callback<FriendInboxResponseModel> {
            override fun onResponse(
                call: Call<FriendInboxResponseModel>,
                response: Response<FriendInboxResponseModel>
            ) {
                println(response.body())
                if (response.isSuccessful)

                    response.body()?.data?.let { friendInboxRecyclerAdapter.updateInbox(it) }
                else
                    print(response.errorBody()?.string())
            }

            override fun onFailure(call: Call<FriendInboxResponseModel>, t: Throwable) {
                Constants.showError(t)
            }
        })
    }
}