package com.example.chuckapp.modules.home.view.appBarNavigation

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chuckapp.BaseActivity
import com.example.chuckapp.R
import com.example.chuckapp.model.requestModels.Home.FriendSearchRequestResponse
import com.example.chuckapp.modules.home.recyclerAdapters.FriendSearchRecyclerAdapter
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.util.Constants
import kotlinx.android.synthetic.main.activity_add_friend_page.*
import retrofit2.Call
import retrofit2.Response


class AddFriendPage : BaseActivity() {
    lateinit var searchListRecyclerAdapter: FriendSearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend_page)

        searchListRecyclerAdapter = FriendSearchRecyclerAdapter(this)

        searchFriendRecyclerView.apply {
            adapter = searchListRecyclerAdapter
            layoutManager = LinearLayoutManager(applicationContext)

        }

        topAppBarFriend.setNavigationOnClickListener {
            onBackPressed()
        }

        searchFriendSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.length > 2) {
                    sendSearchRequest(newText)

                } else {
                    searchListRecyclerAdapter.updateSearchResult(listOf())
                    noSearchResultTextView.visibility = View.GONE
                }

                return true
            }

        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun sendSearchRequest(query: String) {
        HomeClient().getHomeApiService(this).searchFriend(query)
            .enqueue(object : retrofit2.Callback<FriendSearchRequestResponse> {
                override fun onResponse(
                    call: Call<FriendSearchRequestResponse>,
                    response: Response<FriendSearchRequestResponse>
                ) {
                    if (response.isSuccessful) {
                        searchListRecyclerAdapter.updateSearchResult(listOf())
                        println(response.body())
                        response.body()
                            ?.let { searchListRecyclerAdapter.updateSearchResult(it.data) }
                        if (searchListRecyclerAdapter.itemCount == 0)
                            noSearchResultTextView.visibility = View.VISIBLE
                        else noSearchResultTextView.visibility = View.GONE

                    } else
                        println(response.errorBody()?.string())
                }

                override fun onFailure(call: Call<FriendSearchRequestResponse>, t: Throwable) {
                    Constants.showError(t)
                }

            })
    }

}