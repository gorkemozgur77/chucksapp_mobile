package com.example.chuckapp.modules.home.recyclerAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chuckapp.R
import com.example.chuckapp.model.friend.SentRequest
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.util.Constants.getDate
import com.example.chuckapp.util.Constants.showError
import kotlinx.android.synthetic.main.friend_upcoming_row.view.*
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Response

class UpcomingRequestsRecyclerAdapter(val context: Context) :
    RecyclerView.Adapter<UpcomingRequestsRecyclerAdapter.UpcomingRequestsViewHolder>() {
    class UpcomingRequestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var list: MutableList<SentRequest> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingRequestsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.friend_upcoming_row, parent, false)
        return UpcomingRequestsViewHolder(view)
    }

    override fun onBindViewHolder(holder: UpcomingRequestsViewHolder, position: Int) {
        holder.itemView.apply {
            upcomingUserNameTextView.text = list[position].receiver.fullname
            upcomingDateTextView.text = getDate(list[position].time.timestamp)
            upcomingStatusIcon.setOnClickListener {
                cancelFriendRequest(list[position], statusTextView, upcomingStatusIcon, upcomingProgressbar, position)
                statusTextView.text = "CANCELED"
                upcomingStatusIcon.visibility = View.GONE
                upcomingProgressbar.visibility = View.VISIBLE
            }

            if (position == 0)
                upcomingLineer.background = null
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateInbox(list: MutableList<SentRequest>) {
        this.list = list
        notifyDataSetChanged()
    }

    private fun changeLayout( view1: TextView, view2: View, view3: ProgressBar){
        view1.text = "PENDING"
        view2.visibility = View.VISIBLE
        view3.visibility = View.GONE
    }

    private fun cancelFriendRequest(request: SentRequest, view1: TextView, view2: View, view3: ProgressBar, position: Int) {
        val sendTime = System.currentTimeMillis()

        HomeClient().getHomeApiService(context).cancelFriendRequest(request.id)
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    println(response.body())
                    if (response.isSuccessful) {
                        if ((System.currentTimeMillis() - sendTime) < 1500){
                            GlobalScope.launch {
                                delay(1500)
                                withContext(Dispatchers.Main){
                                    list.remove(request)
                                    notifyItemRemoved(position)
                                    notifyItemRangeChanged(position, list.size)
                                    delay(500)
                                    changeLayout(view1, view2, view3)
                                }
                            }
                        }
                        else{
                            list.remove(request)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, list.size)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }

            })

    }
}