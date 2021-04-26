package com.example.chuckapp.modules.home.recyclerAdapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.chuckapp.R
import com.example.chuckapp.model.friend.Friend
import com.example.chuckapp.model.requestModels.Home.SendIdRequestBody
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.modules.home.view.appBarNavigation.AddFriendPage
import com.example.chuckapp.util.Constants
import kotlinx.android.synthetic.main.friends_row.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class FriendSearchRecyclerAdapter(val context: Context) :
    RecyclerView.Adapter<FriendSearchRecyclerAdapter.FriendSearchViewHolder>() {
    class FriendSearchViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView)

    var searchResult: List<Friend> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.friends_row, parent, false)
        return FriendSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendSearchViewHolder, position: Int) {
        holder.itemView.isim.text = searchResult[position].fullName
        holder.itemView.statusCardView.visibility = View.GONE
//        holder.itemView.linear11.setOnClickListener {
//            withButtonCentered(searchResult[position])
//        }
    }

    override fun getItemCount(): Int {
        return searchResult.size
    }

    fun updateSearchResult(searchResult: List<Friend>) {
        this.searchResult = searchResult
        notifyDataSetChanged()
    }


//    private fun withButtonCentered(friend: Friend) {
//
//        val alertDialog = AlertDialog.Builder(context).create()
//        alertDialog.setTitle("Add friend")
//        alertDialog.setMessage("Do you want to add ${friend.fullName}")
//        alertDialog.setButton(
//            AlertDialog.BUTTON_POSITIVE, "Yes"
//        ) { _, _ ->
//            sendFriendRequest(friend.id)
//            (context as AddFriendPage).onBackPressed()
//        }
//
//        alertDialog.setButton(
//            AlertDialog.BUTTON_NEGATIVE, "No"
//        ) { dialog, which -> dialog.dismiss() }
//
//
//        alertDialog.show()
//
//        val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
//        val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//
//
//        val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
//        layoutParams.weight = 10f
//
//        btnPositive.setTextColor(context.resources.getColor(R.color.white))
//        btnPositive.setBackgroundColor(context.resources.getColor(R.color.etoBlue))
//        btnPositive.layoutParams = layoutParams
//        btnNegative.layoutParams = layoutParams
//    }





}