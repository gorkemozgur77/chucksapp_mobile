package com.example.chuckapp.modules.home.view.bottomNavigation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.example.chuckapp.R
import com.example.chuckapp.model.requestModels.auth.LoginResponse
import com.example.chuckapp.model.requestModels.signUp.MeResponse
import com.example.chuckapp.model.requestModels.signUp.SignOutRequest
import com.example.chuckapp.modules.auth.view.AuthActivity
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.modules.home.view.appBarNavigation.AddFriendPage
import com.example.chuckapp.service.SessionManager
import com.example.chuckapp.util.Constants
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_account.*
import retrofit2.Call
import retrofit2.Response
import kotlin.math.abs


class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            getInfo(it)
        }
        accountLogOutbuttonId.setOnClickListener {
            context?.let { it1 -> signOut(it1) }
        }

        accountAppbarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { accountAppbar, verticalOffset ->

            if (abs(verticalOffset) == accountAppbarLayout.totalScrollRange){
                val paddingDp = 15
                val density = context?.getResources()?.getDisplayMetrics()?.density
                val paddingPixel = (paddingDp * density!!).toInt()
                accountTopAppBar.setPadding(paddingPixel,0,0,0)
                accountTopAppBar.setTitle("Account")
            }

            if (abs(verticalOffset) >= accountAppbarLayout.totalScrollRange/3 ) {
                toggle(true)

            } else if (verticalOffset == 0) {
                accountAppbarLayout.setPadding(0,0,0,0);
                accountTopAppBar.title = " "
                accountCollapsingToolbar.isTitleEnabled = false
                toggle(false)

            } else {
                toggle(false)
            }
        })

        accountTopAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.addFriend -> NavigateToAddfriend()
            }
            true
        }
    }

    private fun toggle(show: Boolean) {
        val transition: Transition = Fade()
        transition.duration = 300
        transition.addTarget(accountProfileCardView)
        TransitionManager.beginDelayedTransition(accountCordinator, transition)
        accountProfileCardView.visibility = if (!show) View.VISIBLE else View.GONE
        accountProfileCardView.bringToFront()
    }

    fun NavigateToAddfriend(){
        startActivity(Intent(context, AddFriendPage::class.java))
        activity?.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
    }

    private fun getInfo(context: Context){
        accountProgressBar.visibility = View.VISIBLE
        HomeClient().getHomeApiService(context).getProfile().enqueue(object : retrofit2.Callback<MeResponse> {

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<MeResponse>, response: Response<MeResponse>) {
                accountProgressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    accountNameAndSurnameId.text = response.body()?.user?.isim.toString() + " " + response.body()?.user?.soyisim.toString()
                    accountEmailId.text = response.body()?.user?.eposta.toString()
                }
            }

            override fun onFailure(call: Call<MeResponse>, t: Throwable) {
                Constants.showError(t)
            }


        })


    }
    private fun signOut(context: Context){

        HomeClient().getHomeApiService(context).signOut(SignOutRequest(Constants.PLATFORM_NAME)).enqueue(
            object : retrofit2.Callback<LoginResponse> {

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    println(response.body())
                    println(response.errorBody()?.string())

                    if (response.isSuccessful) {
                        println("cikis yapildi")
                        SessionManager(context).deleteTokens()
                        startActivity(Intent(context, AuthActivity::class.java))
                        activity?.finish()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Constants.showError(t)
                }
            })
    }

}