package com.example.chuckapp.modules.home.recyclerAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chuckapp.R
import com.example.chuckapp.model.friend.SentRequest
import kotlinx.android.synthetic.main.friend_upcoming_row.view.*

class UpcomingRequestsRecyclerAdapter(val context: Context) :
    RecyclerView.Adapter<UpcomingRequestsRecyclerAdapter.UpcomingRequestsViewHolder>() {
    class UpcomingRequestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var list: List<SentRequest> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingRequestsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.friend_upcoming_row, parent, false)
        return UpcomingRequestsViewHolder(view)
    }

    override fun onBindViewHolder(holder: UpcomingRequestsViewHolder, position: Int) {
        holder.itemView.apply {
            upcomingUserNameTextView.text = list[position].receiever.fullname
            upcomingDateTextView.text = "01/06/1997"

            if (position == 0)
                upcomingLineer.background = null
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateInbox(list: List<SentRequest>) {
        this.list = list
        notifyDataSetChanged()
    }
}