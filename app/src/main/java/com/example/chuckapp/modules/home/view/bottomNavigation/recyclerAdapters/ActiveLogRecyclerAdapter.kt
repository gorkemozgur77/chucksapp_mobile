package com.example.chuckapp.modules.home.view.bottomNavigation.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.chuckapp.R
import com.example.chuckapp.model.requestModels.Home.Entry
import kotlinx.android.synthetic.main.activelog_recycyler_row.view.*
import kotlinx.android.synthetic.main.inactivelog_recycler_row.view.*

class ActiveLogRecyclerAdapter () : RecyclerView.Adapter<ActiveLogRecyclerAdapter.LogViewHolder>() {
    class LogViewHolder(ItemView : View) : RecyclerView.ViewHolder(ItemView) {}

    var loglist : List<Entry> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveLogRecyclerAdapter.LogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activelog_recycyler_row,parent,false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActiveLogRecyclerAdapter.LogViewHolder, position: Int) {
        holder.itemView.activeDeviceName.text = loglist[position].deviceInfo
        holder.itemView.activeIpAddress.text = loglist[position].ipAdress
        holder.itemView.activeDate.text = "1 Jan 2018, Friday"
        if (loglist[position].plaformName == "WEB")
            holder.itemView.activePlatformIcon.setCompoundDrawablesWithIntrinsicBounds(null,null,null, ContextCompat.getDrawable(holder.itemView.context,R.drawable.ic_baseline_computer_24))
        else if (loglist[position].plaformName == "MOBILE")
            holder.itemView.activePlatformIcon.setCompoundDrawablesWithIntrinsicBounds(null,null,null, ContextCompat.getDrawable(holder.itemView.context,R.drawable.ic_baseline_phone_iphone_24))
        if (position == 0)
            holder.itemView.activeLineer.background = null
    }

    override fun getItemCount(): Int {
        return loglist.size
     }
    fun setActiveListItems(loglist: List<Entry>){
        this.loglist = loglist;
        notifyDataSetChanged()
    }
}