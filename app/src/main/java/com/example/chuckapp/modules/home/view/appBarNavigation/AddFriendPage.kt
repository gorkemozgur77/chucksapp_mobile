package com.example.chuckapp.modules.home.view.appBarNavigation

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.example.chuckapp.BaseActivity
import com.example.chuckapp.R
import com.example.chuckapp.model.friend.FriendSuggestion
import com.example.chuckapp.model.requestModels.Home.FriendSearchRequestResponse
import com.example.chuckapp.model.requestModels.Home.MeResponse
import com.example.chuckapp.model.requestModels.Home.SendIdRequestBody
import com.example.chuckapp.modules.home.recyclerAdapters.IncomingRequestsRecyclerAdapter
import com.example.chuckapp.modules.home.recyclerAdapters.UpcomingRequestsRecyclerAdapter
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.service.HandlerMe
import com.example.chuckapp.util.Constants
import com.example.chuckapp.util.InboxManager
import kotlinx.android.synthetic.main.activity_add_friend_page.*
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


class AddFriendPage : BaseActivity(), HandlerMe {
    lateinit var incomingRequestsRecyclerAdapter: IncomingRequestsRecyclerAdapter
    lateinit var upcomingRequestsRecyclerAdapter: UpcomingRequestsRecyclerAdapter

    var history: ArrayList<FriendSuggestion> = arrayListOf()
    var lastQuery: String? = "Search..."


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend_page)
        val inboxManager = InboxManager(baseContext)

        setupInboxRecyclerView()
        setupUpcomingRecyclerView()
        setupSearchBar()
        incomingRequestsRecyclerAdapter.updateInbox(inboxManager.fetchReceivedRequests())
        upcomingRequestsRecyclerAdapter.updateInbox(inboxManager.fetchSentRequests())

        topAppBarFriend.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun getWebSocketData(jsonObject: JSONObject) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        floating_search_view.clearSearchFocus()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onPause() {
        super.onPause()
        floating_search_view.clearSearchFocus()
    }

    private fun setupInboxRecyclerView(){
        incomingRequestsRecyclerAdapter = IncomingRequestsRecyclerAdapter(baseContext)
        incomingBoxRecyclerView.apply {
            adapter = incomingRequestsRecyclerAdapter
            layoutManager = LinearLayoutManager(baseContext)
        }
        incomingRequestsRecyclerAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                super.onChanged()
                checkEmpty()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                checkEmpty()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                checkEmpty()
            }

            fun checkEmpty() {
                if (incomingRequestsRecyclerAdapter.itemCount == 0){
                    viewFlipper1.showNext()
                    val a = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 77F, resources.displayMetrics)
                    sendRequestsTextView.setPadding(0, a.toInt(),0,0)
                }

            }
        })
    }

    private fun setupUpcomingRecyclerView(){
        upcomingRequestsRecyclerAdapter = UpcomingRequestsRecyclerAdapter(baseContext)
        sendRequestsRecyclerView.apply {
            adapter = upcomingRequestsRecyclerAdapter
            layoutManager = LinearLayoutManager(baseContext)
        }
        upcomingRequestsRecyclerAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            var flipperChecker = false

            override fun onChanged() {
                super.onChanged()
                checkEmpty()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                checkEmpty()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                checkEmpty()
            }


            fun checkEmpty() {
                if (upcomingRequestsRecyclerAdapter.itemCount == 0){
                    viewFlipper2.showNext()
                    flipperChecker = true
                }
                else if (upcomingRequestsRecyclerAdapter.itemCount > 0 && flipperChecker){
                    viewFlipper2.showPrevious()
                    flipperChecker = false
                }

            }
        })
    }

    private fun setupSearchBar() {
        floating_search_view.apply {
            setOnQueryChangeListener { oldQuery, newQuery ->
                showProgress()
                if (oldQuery.equals("") || newQuery.equals("")) {
                    clearSuggestions()
                }
                if (!oldQuery.equals("") && newQuery.equals(""))
                    clearSuggestions()
                if (newQuery.isEmpty())
                    hideProgress()
                else if (newQuery.length > 2) {
                    HomeClient().getHomeApiService(baseContext).searchFriend(newQuery)
                        .enqueue(object : retrofit2.Callback<FriendSearchRequestResponse> {
                            override fun onResponse(
                                call: Call<FriendSearchRequestResponse>,
                                response: Response<FriendSearchRequestResponse>
                            ) {
                                if (response.isSuccessful && response.body()?.data != null) {
                                    swapSuggestions(response.body()?.data)
                                    lifecycleScope.launch {
                                        delay(2000)
                                        for (friendSuggestion in response.body()!!.data) {
                                            if (!history.any { it.id == friendSuggestion.id })
                                                history.add(friendSuggestion)
                                        }
                                        history.forEach { it.mIsHistory = true }
                                    }

                                    hideProgress()

                                } else {
                                    println(response.errorBody()?.string())
                                    hideProgress()
                                }
                            }

                            override fun onFailure(
                                call: Call<FriendSearchRequestResponse>,
                                t: Throwable
                            ) {
                                Constants.showError(t)
                            }

                        })
                }
            }

            setOnFocusChangeListener(object : FloatingSearchView.OnFocusChangeListener {
                override fun onFocus() {
                    println("focused")
                    swapSuggestions(history)
                }

                override fun onFocusCleared() {
                    setSearchBarTitle(lastQuery)
                    println("cleared")

                }

            })

            setOnSearchListener(object : FloatingSearchView.OnSearchListener {
                override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
                    if (searchSuggestion != null) {
                        println((searchSuggestion as FriendSuggestion).id)
                        searchSuggestion.id?.let { sendFriendRequest(it) }
                        lastQuery = searchSuggestion.body
                        clearFocus()
                    }

                }

                override fun onSearchAction(currentQuery: String?) {
                    lastQuery = currentQuery
                }
            })

            setOnBindSuggestionCallback { _, leftIcon, textView, item, _ ->

                val textColor = "#787878"
                val textLight = "#000000"
                if (history.contains(item)) {
                    leftIcon?.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_history_black_24dp, null
                        )
                    )
                    leftIcon?.alpha = .36f
                } else {
                    leftIcon?.alpha = 0.0f
                    leftIcon?.setImageDrawable(null)
                }
                textView.setCompoundDrawablesWithIntrinsicBounds(
                    0, //left
                    0, //top
                    R.drawable.ic_arrow_back_black_24dp, //right
                    0
                )//bottom

                textView.setTextColor(Color.parseColor(textColor))
                val text: String = item.body!!.replaceFirst(
                    floating_search_view.query,
                    "<font color=\"$textLight\">" + floating_search_view.query
                        .toString() + "</font>"
                )
                textView.text = Html.fromHtml(text)
            }
        }
    }

    private fun sendFriendRequest(id: String) {
        val sentTime = System.currentTimeMillis()
        showProgressBar()
        HomeClient().getHomeApiService(baseContext).addFriend(SendIdRequestBody(id))
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if ((System.currentTimeMillis() - sentTime) < 500){
                        GlobalScope.launch {
                            delay(1000)
                            withContext(Dispatchers.Main){
                                getInfo(baseContext)
                                hideProgressBar()
                            }
                        }
                    }
                    else
                        hideProgressBar()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Constants.showError(t)
                }

            })
    }

    private fun getInfo(context: Context) {
        HomeClient().getHomeApiService(context).getProfile()
            .enqueue(object : retrofit2.Callback<MeResponse> {
                override fun onResponse(call: Call<MeResponse>, response: Response<MeResponse>) {
                    println(response.body())
                    if (response.isSuccessful) {
                        InboxManager(context).deleteAll()
                        val user = response.body()?.user!!
                        InboxManager(context).apply {
                            user.friendRequestInbox?.let {
                                for (request in it.sent){
                                    if (!upcomingRequestsRecyclerAdapter.list.contains(request)){
                                        upcomingRequestsRecyclerAdapter.addRequest(request)
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<MeResponse>, t: Throwable) {
                    Constants.showError(t)
                }
            })
    }

    override fun myMessage(message: Any) {

    }
}