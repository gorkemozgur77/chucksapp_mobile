package com.example.chuckapp.modules.home.recyclerAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.chuckapp.R
import com.example.chuckapp.model.friend.Friend
import com.example.chuckapp.model.requestModels.Home.SendIdRequestBody
import com.example.chuckapp.modules.call.CallSenderActivity
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.util.Constants
import com.example.chuckapp.util.setSafeOnClickListener
import kotlinx.android.synthetic.main.friends_row.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class FriendListRecyclerAdapter(val context: Context) :
    RecyclerView.Adapter<FriendListRecyclerAdapter.FriendListViewHolder>() {
    class FriendListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    var friendList: List<Friend> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.friends_row, parent, false)
        return FriendListViewHolder(view)
    }

    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: FriendListViewHolder, position: Int) {
        holder.itemView.isim.text = friendList[position].fullName
        if (friendList[position].status == "ONLINE")
            ViewCompat.setBackgroundTintList(
                holder.itemView.onlineOfflineFab,
                ColorStateList.valueOf(context.getColor(R.color.green))
            )
        else if (friendList[position].status == "OFFLINE")
            ViewCompat.setBackgroundTintList(
                holder.itemView.onlineOfflineFab,
                ColorStateList.valueOf(context.getColor(R.color.red))
            )


        holder.itemView.setSafeOnClickListener {
            call(friendList[position].id, position)

        }
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
        if (friendList[index].status == status)
            return
        friendList[index].status = status
        notifyItemChanged(index)
    }

    private fun call(id: String, index: Int) {
        HomeClient().getHomeApiService(context).callFriend(SendIdRequestBody(id))
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    println("Response status code: "+response.code())
                    if (response.isSuccessful){

                        val json = JSONObject(response.body()!!.string())
                        val callId = json.getJSONObject("data").getString("id")
                        val intent = Intent(context, CallSenderActivity::class.java)
                        intent.putExtra("call_id", callId)
                        intent.putExtra("friend",friendList[index])
                        context.startActivity(intent)
                    }


                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Constants.showError(t)
                }

            })
    }

}