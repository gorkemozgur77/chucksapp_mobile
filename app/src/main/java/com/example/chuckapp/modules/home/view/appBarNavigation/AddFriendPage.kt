package com.example.chuckapp.modules.home.view.appBarNavigation

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.example.chuckapp.BaseActivity
import com.example.chuckapp.R
import com.example.chuckapp.model.friend.FriendSuggestion
import com.example.chuckapp.model.requestModels.Home.FriendSearchRequestResponse
import com.example.chuckapp.model.requestModels.Home.SendIdRequestBody
import com.example.chuckapp.modules.home.recyclerAdapters.IncomingRequestsRecyclerAdapter
import com.example.chuckapp.modules.home.recyclerAdapters.UpcomingRequestsRecyclerAdapter
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.util.Constants
import com.example.chuckapp.util.InboxManager
import kotlinx.android.synthetic.main.activity_add_friend_page.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


class AddFriendPage : BaseActivity() {
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

        incomingRequestsRecyclerAdapter.updateInbox(inboxManager.fetchReceivedRequests())
        upcomingRequestsRecyclerAdapter.updateInbox(inboxManager.fetchSentRequests())

        topAppBarFriend.setNavigationOnClickListener {
            onBackPressed()
        }

        setupSearchBar()

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
    }

    private fun setupUpcomingRecyclerView(){
        upcomingRequestsRecyclerAdapter = UpcomingRequestsRecyclerAdapter(baseContext)
        sendRequestsRecyclerView.apply {
            adapter = upcomingRequestsRecyclerAdapter
            layoutManager = LinearLayoutManager(baseContext)
        }
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
                                        for (friendSuggeston in response.body()!!.data) {
                                            if (!history.any { it.id == friendSuggeston.id })
                                                history.add(friendSuggeston)
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

            setOnBindSuggestionCallback { suggestionView, leftIcon, textView, item, itemPosition ->

                val a = item
                val textColor = "#787878"
                val textLight = "#000000"
                if (history.contains(item)) {
                    leftIcon?.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_history_black_24dp, null
                        )
                    )
                    leftIcon?.alpha = .36f;
                } else {
                    leftIcon?.alpha = 0.0f;
                    leftIcon?.setImageDrawable(null);
                }
                textView.setCompoundDrawablesWithIntrinsicBounds(
                    0, //left
                    0, //top
                    R.drawable.ic_arrow_back_black_24dp, //right
                    0
                );//bottom

                textView.setTextColor(Color.parseColor(textColor))
                val text: String = a.body!!.replaceFirst(
                    floating_search_view.getQuery(),
                    "<font color=\"$textLight\">" + floating_search_view.query
                        .toString() + "</font>"
                )
                textView.text = Html.fromHtml(text)
            }
        }
    }

    private fun sendFriendRequest(id: String) {
        HomeClient().getHomeApiService(baseContext).addFriend(SendIdRequestBody(id))
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful)
                        println(response.body()?.string())
                    else
                        println(response.errorBody()?.string())
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Constants.showError(t)
                }

            })
    }


}