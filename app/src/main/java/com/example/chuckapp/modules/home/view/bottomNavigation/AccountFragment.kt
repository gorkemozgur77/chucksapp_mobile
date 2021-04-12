package com.example.chuckapp.modules.home.view.bottomNavigation

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.example.chuckapp.R
import com.example.chuckapp.model.requestModels.Home.ActiveResponse
import com.example.chuckapp.model.requestModels.Home.InactiveResponse
import com.example.chuckapp.model.requestModels.Home.MeResponse
import com.example.chuckapp.model.requestModels.auth.LoginResponse
import com.example.chuckapp.model.requestModels.signUp.SignOutRequest
import com.example.chuckapp.modules.auth.view.AuthActivity
import com.example.chuckapp.modules.home.recyclerAdapters.ActiveLogRecyclerAdapter
import com.example.chuckapp.modules.home.recyclerAdapters.InactiveLogRecyclerAdapter
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.modules.home.view.HomePageActivity
import com.example.chuckapp.modules.home.view.appBarNavigation.AddFriendPage
import com.example.chuckapp.service.MyFirebaseInstanceIDService
import com.example.chuckapp.service.SessionManager
import com.example.chuckapp.util.Constants
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.inactivelog_recycler_row.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import kotlin.math.abs


class AccountFragment : Fragment() {


    lateinit var activeRecyclerAdapter: ActiveLogRecyclerAdapter
    lateinit var inactiveRecyclerAdapter: InactiveLogRecyclerAdapter
    var nextUrlForInactiveLogs: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActiveStatusRecyclerView()
        setupInactiveStatusRecyclerView()


        context?.let {
            getActiveLog(it)
            getInactiveLog(it)
            getInfo(it)
        }

        accountLoadMore.setOnClickListener {
            context?.let { nextUrlForInactiveLogs?.let { it1 -> updateInactiveLog(it, it1) } }
        }

        nestedscroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                (activity as HomePageActivity?)?.toggleBottomAppBar(false)
            }
            if (scrollY < oldScrollY) {
                (activity as HomePageActivity?)?.toggleBottomAppBar(true)
            }
        })

        accountAppbarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { accountAppbar, verticalOffset ->

            if (abs(verticalOffset) == accountAppbarLayout.totalScrollRange) {
                val paddingDp = 15
                val density = context?.resources?.displayMetrics?.density
                val paddingPixel = (paddingDp * density!!).toInt()
                accountTopAppBar.setPadding(paddingPixel, 0, 0, 0)
                accountTopAppBar.title = "Account"
            }
            if (abs(verticalOffset) >= accountAppbarLayout.totalScrollRange / 3) {
                toggle(true)

            } else if (verticalOffset == 0) {
                accountAppbarLayout.setPadding(0, 0, 0, 0);
                accountTopAppBar.title = " "
                accountCollapsingToolbar.isTitleEnabled = false
                toggle(false)

            } else {
                toggle(false)
            }
        })

        accountTopAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.addFriend -> NavigateToAddfriend()
            }
            true
        }

        accountTopAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logOut -> context?.let { it1 -> withButtonCentered(it1) }
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

    private fun NavigateToAddfriend() {
        startActivity(Intent(context, AddFriendPage::class.java))
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun setupActiveStatusRecyclerView() {
        activeRecyclerAdapter = ActiveLogRecyclerAdapter()
        accountActiveRecycler.layoutManager = LinearLayoutManager(context)
        accountActiveRecycler.adapter = activeRecyclerAdapter
    }

    private fun setupInactiveStatusRecyclerView() {
        inactiveRecyclerAdapter = InactiveLogRecyclerAdapter()
        accountInactiveRecycler.layoutManager = LinearLayoutManager(context)
        accountInactiveRecycler.adapter = inactiveRecyclerAdapter
    }

    private fun getInfo(context: Context) {
        accountProgressBar.visibility = View.VISIBLE
        HomeClient().getHomeApiService(context).getProfile().enqueue(getInfoResponseHandler)
    }

    private fun signOut(context: Context) {
        HomeClient().getHomeApiService(context).signOut(SignOutRequest(Constants.PLATFORM_NAME))
            .enqueue(signOutResponseHangler)
    }

    private fun getActiveLog(context: Context) {
        HomeClient().getHomeApiService(context).getActiveEntries()
            .enqueue(getActiveLogResponseHandler)
    }

    private fun getInactiveLog(context: Context) {
        HomeClient().getHomeApiService(context).getInactiveEntries()
            .enqueue(getInactiveResponseHandler)
    }

    private fun updateInactiveLog(context: Context, url: String) {
        updateInactiveEntryProgressBar.visibility = View.VISIBLE
        accountLoadMore.visibility = View.GONE
        HomeClient().getHomeApiService(context).getInactiveEntries(url)
            .enqueue(updateInactiveLogResponseHandler)
    }

    private val getInactiveResponseHandler = object : retrofit2.Callback<InactiveResponse> {

        override fun onResponse(
            call: Call<InactiveResponse>,
            response: Response<InactiveResponse>
        ) {
            if (response.isSuccessful) {
                println(response.body())
                response.body()?.entry?.let { inactiveRecyclerAdapter.setInactiveListItems(it) }
                accountLoadMore.visibility = View.VISIBLE
                if (response.body()?.nexturl != null) {
                    nextUrlForInactiveLogs = response.body()!!.nexturl
                } else {
                    nextUrlForInactiveLogs = null
                }
            }
        }

        override fun onFailure(call: Call<InactiveResponse>, t: Throwable) {
            Constants.showError(t)
        }
    }

    private val updateInactiveLogResponseHandler = object : retrofit2.Callback<InactiveResponse> {
        override fun onResponse(
            call: Call<InactiveResponse>,
            response: Response<InactiveResponse>
        ) {

            updateInactiveEntryProgressBar.visibility = View.GONE
            if (response.isSuccessful) {
                println(response.body())
                response.body()?.entry?.let { inactiveRecyclerAdapter.updateInactiveListItems(it) }
                if (response.body()?.nexturl == null) {
                    nextUrlForInactiveLogs = null
                    accountNoMoreResultView.visibility = View.VISIBLE
                } else {
                    nextUrlForInactiveLogs = response.body()!!.nexturl
                    accountLoadMore.visibility = View.VISIBLE
                }
            } else {
                val jObjError = JSONObject(response.errorBody()?.string())
                if (jObjError.getString("message") == "Only < 5 page sizes allowed") {
                    nextUrlForInactiveLogs = null
                    accountNoMoreResultView.visibility = View.VISIBLE
                }
            }
        }

        override fun onFailure(call: Call<InactiveResponse>, t: Throwable) {
            Constants.showError(t)
        }
    }

    private val getActiveLogResponseHandler = object : retrofit2.Callback<ActiveResponse> {


        override fun onResponse(call: Call<ActiveResponse>, response: Response<ActiveResponse>) {
            if (response.isSuccessful) {
                println(response.body())
                response.body()?.entry?.let { activeRecyclerAdapter.setActiveListItems(it) }
            }
        }

        override fun onFailure(call: Call<ActiveResponse>, t: Throwable) {
            Constants.showError(t)
        }
    }

    private val signOutResponseHangler = object : retrofit2.Callback<LoginResponse> {

        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            if (response.isSuccessful) {
                println(response.body())
                context?.let { SessionManager(it).deleteTokens() }
                FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener {
                    if (it.isSuccessful){
                        println(it.result)
                        FirebaseMessaging.getInstance().deleteToken()
                    }

                    else
                        println("Silinemedi")
                }
                startActivity(Intent(context, AuthActivity::class.java))
                activity?.finish()
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Constants.showError(t)
        }

    }

    private val getInfoResponseHandler = object : retrofit2.Callback<MeResponse> {

        @SuppressLint("SetTextI18n")
        override fun onResponse(call: Call<MeResponse>, response: Response<MeResponse>) {
            accountProgressBar.visibility = View.GONE
            if (response.isSuccessful) {
                accountNameAndSurnameId.text =
                    response.body()?.user?.isim.toString() + " " + response.body()?.user?.soyisim.toString()
                accountEmailId.text = response.body()?.user?.eposta.toString()
            }
        }

        override fun onFailure(call: Call<MeResponse>, t: Throwable) {
            Constants.showError(t)
        }
    }

    fun withButtonCentered(context: Context) {

        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle("Log Out")
        alertDialog.setMessage("Do you want to log out?")

        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "Yes"
        ) { dialog, which -> signOut(context) }

        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, "No"
        ) { dialog, which -> dialog.dismiss() }
        alertDialog.show()

        val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)


        val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 10f

        btnPositive.setTextColor(resources.getColor(R.color.white))
        btnPositive.setBackgroundColor(resources.getColor(R.color.red))
        btnPositive.layoutParams = layoutParams
        btnNegative.layoutParams = layoutParams
    }
}