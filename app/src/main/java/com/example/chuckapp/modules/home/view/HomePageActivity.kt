package com.example.chuckapp.modules.home.view

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.example.chuckapp.BaseActivity
import com.example.chuckapp.R
import com.example.chuckapp.model.User
import com.example.chuckapp.model.requestModels.Home.MeResponse
import com.example.chuckapp.modules.home.recyclerAdapters.FriendListRecyclerAdapter
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.modules.home.view.bottomNavigation.AccountFragment
import com.example.chuckapp.modules.home.view.bottomNavigation.FriendsFragment
import com.example.chuckapp.modules.twilio.VideoActivity
import com.example.chuckapp.service.StatusService
import com.example.chuckapp.util.Constants
import com.example.chuckapp.util.InboxManager
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


class HomePageActivity : BaseActivity() {
    lateinit var friendListRecyclerAdapter: FriendListRecyclerAdapter
    lateinit var user: User


    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        Intent(this, StatusService::class.java).also {
            startService(it)
        }

        LocalBroadcastManager.getInstance(baseContext).registerReceiver(
            (messageReceiver),
            IntentFilter("StatusChangeData")
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        friendListRecyclerAdapter = FriendListRecyclerAdapter(this)
        home_page_bottomNavigationViewId.background = null
        home_page_bottomNavigationViewId.menu.getItem(1).isEnabled = false
        makeCurrentFragment(FriendsFragment())

        home_page_bottomNavigationViewId.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.message -> {
                    makeCurrentFragment(FriendsFragment())
                    if (friendListRecyclerAdapter.itemCount == 0)
                        welcomeMessageTextView.visibility = View.VISIBLE
                }
                R.id.account -> {
                    makeCurrentFragment(AccountFragment())
                    welcomeMessageTextView.visibility = View.GONE
                }
            }
            true
        }
        home_page_bottomNavigationViewId.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.message -> print("")
                R.id.account -> print("")
            }
        }
        fabId.setOnClickListener {

        }
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        getInfo(baseContext)
        StatusService.setPage("homepage")
    }

    override fun getWebSocketData(jsonObject: JSONObject) {
        when(jsonObject["event"]){
            "CONNECT" -> {
                friendListRecyclerAdapter.changeStatus(jsonObject["userId"].toString(), "ONLINE")
            }
            "CLOSE" -> {
                friendListRecyclerAdapter.changeStatus(jsonObject["userId"].toString(), "OFFLINE")
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun onDestroy() {
        Intent(this, StatusService::class.java).also {
            stopService(it)
        }
        super.onDestroy()
    }

    fun toggleBottomAppBar(show: Boolean) {
        val transition: Transition = Fade()
        transition.duration = 100
        transition.addTarget(home_page_bottomAppbarId)
        TransitionManager.beginDelayedTransition(homepageLayId, transition)
        if (show) {
            home_page_bottomAppbarId.performShow()
            fabId.show()
        } else {
            home_page_bottomAppbarId.performHide()
            fabId.hide()
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayId, fragment)
            commit()
        }
    }

    fun getInfo(context: Context) {
        showProgressBar()
        HomeClient().getHomeApiService(context).getProfile()
            .enqueue(object : retrofit2.Callback<MeResponse> {
                override fun onResponse(call: Call<MeResponse>, response: Response<MeResponse>) {
                    val item = topAppBar.menu.findItem(R.id.addFriend).actionView
                    val textView = item.findViewById<TextView>(R.id.cart_badge)
                    if (response.isSuccessful) {
                        InboxManager(context).deleteAll()
                        user = response.body()?.user!!
                        InboxManager(context).apply {
                            user.friends?.let { saveFriendList(it) }
                            user.friendRequestInbox?.let {
                                saveInbox(it)
                                val badgeSize = it.received.size
                                textView.apply {
                                    if (badgeSize == 0) {
                                        if (visibility != View.GONE)
                                            visibility = View.GONE
                                    } else {
                                        text = badgeSize.toString()
                                        if (visibility != View.VISIBLE)
                                            visibility = View.VISIBLE
                                    }
                                }
                            }
                            friendListRecyclerAdapter.updateFriendList(fetchFriends())
                        }
                        if (friendListRecyclerAdapter.itemCount == 0)
                            welcomeMessageTextView.visibility = View.VISIBLE
                        else
                            welcomeMessageTextView.visibility = View.GONE
                    }
                    hideProgressBar()
                }
                override fun onFailure(call: Call<MeResponse>, t: Throwable) {
                    Constants.showError(t)
                }
            })
    }
}


