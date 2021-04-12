package com.example.chuckapp.modules.home.recyclerAdapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.chuckapp.R
import com.example.chuckapp.model.friend.FriendRequest
import com.example.chuckapp.model.friend.Sender
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.util.Constants
import kotlinx.android.synthetic.main.friends_row.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class FriendInboxRecyclerAdapter(val context: Context) :
    RecyclerView.Adapter<FriendInboxRecyclerAdapter.FriendInboxViewHolder>() {
    class FriendInboxViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView)

    var inboxResult: List<FriendRequest> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendInboxViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.friends_row, parent, false)
        return FriendInboxViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendInboxViewHolder, position: Int) {
        holder.itemView.isim.text = inboxResult[position].sender.fullname
        holder.itemView.friendsRowCardView.strokeWidth = 0
        holder.itemView.linear11.setOnClickListener {
            withButtonCentered(inboxResult[position])
        }
    }

    override fun getItemCount(): Int {
        return inboxResult.size
    }

    fun updateInbox(inboxResult: List<FriendRequest>) {
        this.inboxResult = inboxResult
        notifyDataSetChanged()
    }

    private fun withButtonCentered(friend: FriendRequest) {

        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle("Add friend")
        alertDialog.setMessage("Do you want to accept ${friend.sender.fullname}")
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "Yes"
        ) { _, _ ->
            acceptFriendRequest(friend.id)
        }

        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, "No"
        ) { dialog, which -> dialog.dismiss() }


        alertDialog.show()

        val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)


        val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 10f

        btnPositive.setTextColor(context.resources.getColor(R.color.white))
        btnPositive.setBackgroundColor(context.resources.getColor(R.color.etoBlue))
        btnPositive.layoutParams = layoutParams
        btnNegative.layoutParams = layoutParams
    }

    private fun acceptFriendRequest(id: String) {
        HomeClient().getHomeApiService(context).acceptFriendRequest(id)
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful)
                        println(response.body().toString())
                    else
                        println(response.errorBody()?.string())
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Constants.showError(t)
                }

            })
    }
}