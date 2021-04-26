package com.example.chuckapp.modules.home.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
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
import com.example.chuckapp.modules.home.view.bottomNavigation.Bottomfragment1
import com.example.chuckapp.modules.twilio.VideoActivity
import com.example.chuckapp.util.Constants
import com.example.chuckapp.util.InboxManager
import kotlinx.android.synthetic.main.activity_home_page.*
import retrofit2.Call
import retrofit2.Response


class HomePageActivity : BaseActivity() {
    lateinit var friendListRecyclerAdapter: FriendListRecyclerAdapter
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        friendListRecyclerAdapter = FriendListRecyclerAdapter(this)
        home_page_bottomNavigationViewId.background = null
        home_page_bottomNavigationViewId.menu.getItem(1).isEnabled = false
        makeCurrentFragment(Bottomfragment1())

        home_page_bottomNavigationViewId.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.message -> {
                    makeCurrentFragment(Bottomfragment1())
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
            startActivity(Intent(baseContext, VideoActivity::class.java))
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

    override fun onStop() {

        super.onStop()


    }

    override fun onStart() {
        super.onStart()
        getInfo(baseContext)


        isUserOnline = false

        LocalBroadcastManager.getInstance(baseContext).registerReceiver(
            (mMessageReceiver),
            IntentFilter("MyData")
        )

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
        HomeClient().getHomeApiService(context).getProfile()
            .enqueue(object : retrofit2.Callback<MeResponse> {

                override fun onResponse(call: Call<MeResponse>, response: Response<MeResponse>) {
                    println(response.body())
                    if (response.isSuccessful) {
                        InboxManager(baseContext).deleteAll()

                        user = response.body()?.user!!

                        InboxManager(baseContext).apply {

                            user.friends?.let { saveFriendList(it) }
                            user.friendRequestInbox?.let { saveInbox(it) }
                            friendListRecyclerAdapter.updateFriendList(fetchFriends())
                        }

                        if (friendListRecyclerAdapter.itemCount == 0)
                            welcomeMessageTextView.visibility = View.VISIBLE
                        else
                            welcomeMessageTextView.visibility = View.GONE
                    }

                }

                override fun onFailure(call: Call<MeResponse>, t: Throwable) {
                    Constants.showError(t)
                }
            })
    }
}


