package com.example.chuckapp.modules.home.recyclerAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chuckapp.R
import com.example.chuckapp.model.friend.Friend
import kotlinx.android.synthetic.main.friends_row.view.*

class FriendListRecyclerAdapter(val context: Context) :
    RecyclerView.Adapter<FriendListRecyclerAdapter.FriendListViewHolder>() {
    class FriendListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var friendList: List<Friend> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.friends_row, parent, false)
        return FriendListViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendListViewHolder, position: Int) {
        holder.itemView.isim.text = friendList[position].fullname
        if (friendList[position].status == "ONLINE")
            holder.itemView.friendsRowCardView.strokeColor = context.getColor(R.color.green)
        else
            holder.itemView.friendsRowCardView.strokeColor = context.getColor(R.color.red)

    }


    override fun getItemCount(): Int {
        return friendList.size
    }

    fun updateFriendList(friendList: List<Friend>) {
        this.friendList = friendList
        notifyDataSetChanged()
    }


    fun changeStatus(id: String, status: String) {
        val index = friendList.indexOf(friendList.filter { it.id == id }[0])
        friendList[index].status = status
        notifyItemChanged(index)
    }

}