package com.example.chuckapp.modules.home.recyclerAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.chuckapp.R
import com.example.chuckapp.model.friend.FriendRequest
import com.example.chuckapp.model.friend.ReceivedRequest
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.modules.home.view.appBarNavigation.FriendInboxActivity
import com.example.chuckapp.util.Constants
import com.example.chuckapp.util.setSafeOnClickListener
import kotlinx.android.synthetic.main.activelog_recycyler_row.view.*
import kotlinx.android.synthetic.main.friend_request_row.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class IncomingRequestsRecyclerAdapter(val context: Context) :
    RecyclerView.Adapter<IncomingRequestsRecyclerAdapter.IncomingRequestsViewHolder>() {
    class IncomingRequestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var list: List<ReceivedRequest> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomingRequestsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.friend_request_row, parent, false)
        return IncomingRequestsViewHolder(view)
    }

    override fun onBindViewHolder(holder: IncomingRequestsViewHolder, position: Int) {
        holder.itemView.apply {

            userNameTextView.text = list[position].sender.fullname
            dateTextView.text = "01/06/1997"

            acceptButton.setSafeOnClickListener {
                acceptFriendRequest(list[position].id)
            }

            if (position == 0)
                inboxLineer.background = null

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateInbox(list: List<ReceivedRequest>) {
        this.list = list
        notifyDataSetChanged()
    }

    private fun acceptFriendRequest(id: String) {
        HomeClient().getHomeApiService(context).acceptFriendRequest(id)
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        println(response.body().toString())
                    } else
                        println(response.errorBody()?.string())
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Constants.showError(t)
                }

            })
    }
}