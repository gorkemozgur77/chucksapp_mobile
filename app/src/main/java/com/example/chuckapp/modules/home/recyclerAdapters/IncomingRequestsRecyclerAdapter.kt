package com.example.chuckapp.modules.home.recyclerAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chuckapp.R
import com.example.chuckapp.model.friend.ReceivedRequest
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.util.Constants.getDate
import com.example.chuckapp.util.Constants.showError
import com.example.chuckapp.util.setSafeOnClickListener
import kotlinx.android.synthetic.main.friend_request_row.view.*
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class IncomingRequestsRecyclerAdapter(val context: Context) :
    RecyclerView.Adapter<IncomingRequestsRecyclerAdapter.IncomingRequestsViewHolder>() {
    class IncomingRequestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var list: MutableList<ReceivedRequest> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomingRequestsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.friend_request_row, parent, false)
        return IncomingRequestsViewHolder(view)
    }

    override fun onBindViewHolder(holder: IncomingRequestsViewHolder, position: Int) {
        holder.itemView.apply {

            userNameTextView.text = list[position].sender.fullname
            dateTextView.text = getDate(list[position].time.timestamp)

            acceptButton.setSafeOnClickListener {
                acceptFriendRequest(
                    list[position],
                    statusProgressbar,
                    rejectOrAcceptButton,
                    position
                )
            }

            rejectButton.setSafeOnClickListener {
                rejectFriendRequest(
                    list[position],
                    statusProgressbar,
                    rejectOrAcceptButton,
                    position
                )
            }

            if (position == 0)
                inboxLineer.background = null
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateInbox(list: MutableList<ReceivedRequest>) {
        this.list = list
        notifyDataSetChanged()
    }

    private fun acceptFriendRequest(
        request: ReceivedRequest,
        view: ProgressBar,
        view2: View,
        position: Int
    ) {
        val sendTime = System.currentTimeMillis()

        HomeClient().getHomeApiService(context).acceptFriendRequest(request.id)
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        view.visibility = View.VISIBLE
                        view2.visibility = View.GONE

                        if ((System.currentTimeMillis() - sendTime) < 1500) {
                            GlobalScope.launch {
                                delay(1500)
                                withContext(Dispatchers.Main) {
                                    list.remove(request)
                                    notifyItemRemoved(position)
                                    notifyItemRangeChanged(position, list.size)
                                }
                            }
                        }

                    } else
                        println(response.errorBody()?.string())
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    showError(t)
                }

            })
    }

    private fun rejectFriendRequest(
        request: ReceivedRequest,
        view: ProgressBar,
        view2: View,
        position: Int
    ) {
        val sendTime = System.currentTimeMillis()
        HomeClient().getHomeApiService(context).rejectFriendRequest(request.id)
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    println(response.code())
                    if (response.isSuccessful) {
                        view.visibility = View.VISIBLE
                        view2.visibility = View.GONE

                        if ((System.currentTimeMillis() - sendTime) < 1500) {
                            GlobalScope.launch {
                                delay(1500)
                                withContext(Dispatchers.Main) {
                                    list.remove(request)
                                    notifyItemRemoved(position)
                                    notifyItemRangeChanged(position, list.size)
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    showError(t)
                }

            })

    }


}